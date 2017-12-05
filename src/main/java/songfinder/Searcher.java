package songfinder;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeSet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Generics.ReentrantLock;
import Libraries.MyMusicLibrary;

public class Searcher {
	
	/**
	 * Declares private data members
	 */
	private MyMusicLibrary library;
	private ReentrantLock rwl;
	
	/**
	 * Initialises private data members
	 * @param ml
	 */
	public Searcher(MyMusicLibrary ml) {
		 this.library = ml;
		 this.rwl = new ReentrantLock();
		
	}

	/**
	 * Private method takes as input an file 
	 * with the list of artists, titles and tags 
	 * to search
	 * 
	 * Returns a JsonObject with JsonArray's of searched items
	 * @param inputFile
	 * @return
	 */
	private JsonObject search(String inputFile) {
		
		Path path = Paths.get(inputFile);
		JsonObject dummy = new JsonObject();
		JsonObject searched = new JsonObject();
		JsonArray artistArray = new JsonArray();
		JsonArray titleArray = new JsonArray();
		JsonArray tagArray = new JsonArray();
			
		if (!path.toString().toLowerCase().endsWith(".json")) {
			System.out.println("Not a json file");
			
		} else { 
				
			try (FileReader fr = new FileReader(path.toFile().getAbsolutePath())) {
				
				JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(fr);
				JsonObject obj = element.getAsJsonObject();
				
				JsonArray getArtist = obj.getAsJsonArray("searchByArtist");
				JsonArray getTitle = obj.getAsJsonArray("searchByTitle");
				JsonArray getTag = obj.getAsJsonArray("searchByTag");
				
				if (getArtist != null) {
					for (int i = 0; i < getArtist.size(); i++) {
						dummy = library.searchHelper(getArtist.get(i).getAsString(), "artist");	
						artistArray.add(dummy);
					}
				}	
				
				if (getTitle != null) {
					for (int i = 0; i < getTitle.size(); i++) {
						dummy = library.searchHelper(getTitle.get(i).getAsString(), "title");
						titleArray.add(dummy);
					}
				}	
				
				if (getTag != null) {
					for (int i = 0; i < getTag.size(); i++) {
						
						dummy = library.searchByTag(getTag.get(i).getAsString());
						tagArray.add(dummy);
					}
				}	
				
				if (artistArray.size() != 0) {
					searched.add("searchByArtist", artistArray);
				}
				
				if (titleArray.size() != 0) {
					searched.add("searchByTitle", titleArray);
				}	
				
				if (tagArray.size() != 0) {
					searched.add("searchByTag", tagArray);
				}
				
			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		return searched;
		
	}
	
	
	/**
	 * Public method calls private methods to 
	 * help search items in input file
	 * and writes to the output file 
	 * @param inputFile
	 * @param outputFile
	 */
	public void searchResultsOutput(String inputFile, String outputFile) {
		
		if (inputFile != null && outputFile != null) {
		
			Path outPath = Paths.get(outputFile);
			outPath.getParent().toFile().mkdirs();
			
			try {
				
				this.rwl.lockRead();
			
				try (BufferedWriter out = Files.newBufferedWriter(outPath)) {
					
					out.write(search(inputFile).toString());
					
				} catch (IOException e) {
				e.getMessage();
				}
			} finally {
				this.rwl.unlockRead();
			}
		}	
	}
}