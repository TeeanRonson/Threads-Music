package songfinder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.TreeSet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import BaseObjects.SingleArtistInfo;
import BaseObjects.SingleSongInfo;
import Libraries.MyArtistLibrary;
import Libraries.MyMusicLibrary;
import Sockets.HttpFetcher;


/**
 * Worker class that implements Runnable
 * Each new Worker object will be passed into 
 * the WorkQueue method 'execute'.
 * 
 * Private data members 
 * @author Rong
 *
 */
public class SongWorker implements Runnable {
	
	private MyMusicLibrary library; 
	private Path p;
//	private MyArtistLibrary aLibrary;
	
	/**
	 * Initialises two private data members
	 * to be the values passed into the constructor 
	 * @param library
	 * @param p
	 */
	public SongWorker(MyMusicLibrary library, Path p) {
		this.library = library;
		this.p = p;
//		this.aLibrary = aLibrary;
	}

	
	/**Run method of the Worker class
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
	@Override
	public void run() {
		
		SingleSongInfo ssi = null;
		SingleArtistInfo sai = null;
		TreeSet<String> tagList = new TreeSet<String>();
		TreeSet<String> similarSongs = new TreeSet<String>();
	
		if (p.toString().toLowerCase().endsWith(".json")) {
			
			try (FileReader fr = new FileReader(p.toFile().getAbsolutePath())) {
				
				JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(fr);
				JsonObject obj = element.getAsJsonObject();
			
				String artistName = obj.get("artist").getAsString();
				String title = obj.get("title").getAsString();
				String trackId = obj.get("track_id").getAsString();
				JsonArray getTags = obj.getAsJsonArray("tags");
				JsonArray getSimilars = obj.getAsJsonArray("similars");
				String tag = "";
				String similar = "";
				
				
				for (int i = 0; i < getTags.size(); i++) {
					JsonArray internalArray = (JsonArray) getTags.get(i);
					tag = internalArray.get(0).getAsString();
					tagList.add(tag);
				}
				
				for (int i = 0; i < getSimilars.size(); i++) {
					JsonArray internalArray = (JsonArray) getSimilars.get(i);
					similar = internalArray.get(0).getAsString();
					similarSongs.add(similar);	
				}
				
				ssi = new SingleSongInfo(artistName, title, trackId, tagList, similarSongs);
			
				if (ssi != null) {
					this.library.addNewSong(ssi);
				}
				
			} catch (IOException e) {
				e.getMessage();
				System.out.println("Can't find file");
			} 
		}
	}
}
