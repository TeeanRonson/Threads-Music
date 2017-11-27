package songfinder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.TreeSet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
public class Worker implements Runnable {
	
	private MyLibrary library; 
	private Path p;
	private MyArtistLibrary addToArtiLibrary; 
	
	
	/**
	 * Initialises two private data members
	 * to be the values passed into the constructor 
	 * @param library
	 * @param p
	 */
	public Worker(MyLibrary library, Path p, MyArtistLibrary addToALibrary) {
		this.library = library;
		this.p = p;
		this.addToArtiLibrary = addToALibrary;
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
			
				String artist = obj.get("artist").getAsString();
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
				
				ssi = new SingleSongInfo(artist, title, trackId, tagList, similarSongs);
			
				if (ssi != null) {
					this.library.addNewSong(ssi);
				}
				
//				String page = "";
//				String madonna = "Madonna";
//				String track = "Holiday";
//				String host = "ws.audioscrobbler.com";
//				String APIKEY = "198f79089ba38013804c115cdebcc857";
//			
//				page = HttpFetcher.download(host, "/2.0?artist=" + madonna +  
//						"&track=" + track + 
//						"&api_key=" + APIKEY + 
//						"&method=track.getInfo&format=json");
//					
//				if (page.contains("HTTP/1.1 200 OK")) {
//					int start = page.indexOf("\n\n");
//					page = page.substring(start);
//						
//					JsonParser parser1 = new JsonParser();
//					JsonElement element1 = parser1.parse(page);
//					JsonObject obj1 = element1.getAsJsonObject();
//					
//					int listeners = obj1.get("listeners").getAsInt();
//					int playCount = obj1.get("playcount").getAsInt();
//					String bio = obj1.get("wiki").getAsString();
//					
//					sai = new SingleArtistInfo(artist, listeners, playCount, bio); 
//					
//					if (sai != null) {
//						this.addToArtiLibrary.addNewArtist(sai);
//					}	
//				}
				
			} catch (IOException e) {
				e.getMessage();
				System.out.println("Can't find file");
			}
		}
	}
}
