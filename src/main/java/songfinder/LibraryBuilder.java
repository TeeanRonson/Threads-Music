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
	private MyLibrary addToLibrary;

	
	public LibraryBuilder() {
		this.addToLibrary = new MyLibrary();
	}
	
	public MyLibrary buildLibrary(String directory) {
		
		MyLibrary ml = new MyLibrary();
		
		// converts the path String into a Path object
		Path path = Paths.get(directory);
		
		try (Stream<Path> paths = Files.walk(path)) {
			
			//use aggregate operation forEach to process each file 
			paths.forEach(p -> processPath(p));
			// if process is successful then add to MyLibrary
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		
		return ml;
	}

	public SingleSongInfo processPath(Path p) {
		
		SingleSongInfo ssi = null;
		ArrayList<String> tagList = new ArrayList<String>();
	
		if (p.toString().toLowerCase().endsWith(".json")) {
		
			Gson gson = new Gson();
			try (FileReader fr = new FileReader(p.toFile().getAbsolutePath())) {
				
				JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(fr);
				JsonObject obj = element.getAsJsonObject();
			
				String artist = obj.get("artist").getAsString();
				String title = obj.get("title").getAsString();
				String trackId = obj.get("track_id").getAsString();
				String tags = "";
				
				JsonArray getTags = obj.getAsJsonArray("tags");
				for (int i = 0; i < getTags.size(); i++) {
					JsonArray internalArray = (JsonArray) getTags.get(i);
					tags = internalArray.get(0).getAsString();
					tagList.add(tags);
				}		
				
				ssi = new SingleSongInfo(artist, title, trackId, tagList);
				
//				System.out.println(ssi);
				if (ssi != null) {
					this.addToLibrary.addNewSong(ssi);
	
				}
			} catch (IOException e) {
				e.getMessage();
				System.out.println("can't find file");
			}
		}
		return ssi;
	}
}
