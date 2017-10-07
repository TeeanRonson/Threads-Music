package songfinder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
		
		try (Stream<Path> paths = Files.walk(path)) {
			
			//use aggregate operation forEach to process each file 
			paths.forEach(p -> processPath(p));
			 
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
//		this.processDirectoryFiles(ml, path);
		
		
		return ml;
	}

	public SingleSongInfo processPath(Path p) {
		
		// if p is a directory we need to do something about it 
//		System.out.println(currentFile.getAbsolutePath());
		
//		System.out.println(p.getFileName());
		SingleSongInfo ssi = null;
		JsonElement sample;
		String tags = "";
	
		if (p.toString().toLowerCase().endsWith(".json")) {
//			System.out.println(p);
		
			Gson gson = new Gson();
//			try (FileReader fr = new FileReader(currentFile.getAbsolutePath())) {
			try (FileReader fr = new FileReader(p.toFile().getAbsolutePath())) {
				
				JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(fr);
				JsonObject obj = element.getAsJsonObject();
			
				String artist = obj.get("artist").getAsString();
				String title = obj.get("title").getAsString();
				String trackId = obj.get("track_id").getAsString();
				
				JsonArray getTags = obj.getAsJsonArray("tags");
//				System.out.println(getTags.size());
				for (int i = 0; i < getTags.size(); i++) {
					JsonArray internalArray = (JsonArray) getTags.get(i);
					System.out.println(internalArray.size());
					tags = internalArray.get(0).getAsString();
//					addTag(tags)		
				}
				
//				System.out.println(getTags);
//				if (getTags.isJsonArray()) {
					
							
				}
				
				ssi = new SingleSongInfo(artist, title, trackId, tags);
			
			} catch (IOException e) {
				e.getMessage();
				System.out.println("can't find file");
			}
//		}
		return ssi;
	
	}
}
