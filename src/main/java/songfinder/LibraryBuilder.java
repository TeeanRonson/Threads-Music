package songfinder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LibraryBuilder {

	
	public LibraryBuilder() { 
		
	}
	//"/Users/Rong/git/lab-3-TeeanRonson/SongFinder/input"
	
	public MyLibrary buildLibrary(String directory) {
		
		MyLibrary ml = new MyLibrary();
		
		// converts the path String into a Path object
		Path path = Paths.get(directory);
		
		// walking the file tree and get a Stream of the Path object
		try (Stream<Path> paths = Files.walk(path)) {
			
			System.out.println(paths);
			// forEach performs an operation for each file/element in paths
			paths.forEach(p -> processPath(p));
			
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
				
		}
		
		
		
		return ml;
	}

	public SingleSongInfo processPath(Path p) {
		
		System.out.println(p.getFileName());
		
		SingleSongInfo ssi = null;
		Path fileName;
		
		
		try (FileReader fr = new FileReader(p.toFile().getAbsolutePath())) {
			
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(fr);
			JsonObject obj = element.getAsJsonObject();
			
			String artist = obj.get("artist").getAsString();
			String title = obj.get("title").getAsString();
			String trackId = obj.get("track_Id").getAsString();
			
			ssi = new SingleSongInfo(artist, title, trackId);
			
		} catch (IOException e) {
			e.getMessage();
			System.out.println("can't find file");
		}
		
		return ssi;
		
	}
	
	
	
}


