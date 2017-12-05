package BaseObjects;

import java.util.ArrayList;
import java.util.TreeSet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/** 
 * Declares private data members 
 * @author Rong
 */
public class SingleSongInfo {
	private String artist; 
	private String title; 
	private String trackId;
	private TreeSet<String> tagList;
	private TreeSet<String> similarSongs;
	 
	/**
	 * Constructor that takes in four inputs: 
	 * 3 String objects and a ArrayList<String> object
	 * @param artist
	 * @param title
	 * @param trackId
	 * @param tagList
	 */
	public SingleSongInfo(String artist, String title, String trackId, TreeSet<String> tagList, TreeSet<String> similarSongs) {
		this.artist = artist; 
		this.title = title;
		this.trackId = trackId;
		this.tagList = tagList;
		this.similarSongs = similarSongs;
	}

	/**
	 * Returns the artist
	 * @return
	 */
	public String getArtist() {
		return this.artist;
	}
	
	/**
	 * Returns the title
	 * @return
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Returns the trackId
	 * @return
	 */
	public String getTrackId() {
		return this.trackId;
	}
	
	/**
	 * Returns a TreeSet of tags 
	 * related to the SingleSongInfo object
	 * @return
	 */
	public TreeSet<String> getTagList() {
		return this.tagList;
	}
	
	/**
	 * Returns a TreeSet of similar songs 
	 * related to the SingleSongInfo object
	 * @return
	 */
	public TreeSet<String> getSimilarSongs() {
		return this.similarSongs;
	}
	
	/**
	 * Displays the SingleSongInfo Object 
	 * information in a String
	 */
	public String toString() {
		return "Artist: " + this.artist + " \nTitle: " + this.title + " \nTrackId: " + this.trackId + "\n";
	}
	
	/**
	 * Creates a JsonObject with all the information
	 * related to a SingleSongInfo object
	 * @return
	 */
	public JsonObject toJson() {
		
		JsonObject object = new JsonObject();
		JsonArray tagList = new JsonArray();
		JsonArray similarSongs = new JsonArray();
		
		object.addProperty("artist", getArtist());
		object.addProperty("title", getTitle());
		object.addProperty("trackId", getTrackId());
		
		for (String x: getTagList()) {
			tagList.add(x);
		}
		object.add("tags", tagList);
			
		for (String x: getSimilarSongs()) {
			similarSongs.add(x);
		}
		object.add("similars", similarSongs);
	
		return object;
		
		
	}
}
