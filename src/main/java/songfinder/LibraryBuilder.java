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
import java.util.TreeSet;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LibraryBuilder {
	
	/**
	 * Declares a private data member
	 */
	private MyLibrary addToLibrary;
	
	/**
	 * Constructor takes no input and initialises
	 * the private data member
	 */
	public LibraryBuilder() {
		this.addToLibrary = new MyLibrary();
	}
	
	/**
	 * Public method takes as input the input directory. 
	 * Converts the directory into a Path object and 
	 * walks the file path to extract all files in 
	 * all sub-folders
	 * @param directory
	 * @return
	 */
	public MyLibrary buildLibrary(String directory) {
		
		Path path = Paths.get(directory);
		
		try (Stream<Path> paths = Files.walk(path)) {
			
			paths.forEach(p -> processPath(p));
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return this.addToLibrary;
	}

	/**
	 * Public method takes as input a path object. 
	 * Processes only .json files
	 * Reads in data from the .json files and returns 
	 * a SingleSongInfo object with the extracted 
	 * information. 
	 * 
	 * Adds the valid SingleSongInfo object to 
	 * MyLibrary class
	 * 
	 * Used sources: 
	 * https://stackoverflow.com/questions/24179163/parse-json-of-two-dimensional-array-in-java
	 * https://stackoverflow.com/questions/18544133/parsing-json-array-into-java-util-list-with-gson
	 * @param p
	 * @return
	 */
	public SingleSongInfo processPath(Path p) {
		
		SingleSongInfo ssi = null;
		TreeSet<String> tagList = new TreeSet<String>();
	
		if (p.toString().toLowerCase().endsWith(".json")) {
			Gson gson = new Gson();
			
			try (FileReader fr = new FileReader(p.toFile().getAbsolutePath())) {
				
				JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(fr);
				JsonObject obj = element.getAsJsonObject();
			
				String artist = obj.get("artist").getAsString();
				String title = obj.get("title").getAsString();
				String trackId = obj.get("track_id").getAsString();
				JsonArray getTags = obj.getAsJsonArray("tags");
				String tags = "";
				
				for (int i = 0; i < getTags.size(); i++) {
					JsonArray internalArray = (JsonArray) getTags.get(i);
					tags = internalArray.get(0).getAsString();
					tagList.add(tags);
				}		
				
				ssi = new SingleSongInfo(artist, title, trackId, tagList);
			
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
