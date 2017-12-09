package BaseObjects;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/** 
 * SingleArtistInfo stores all information about an Artist
 * @author Rong
 *
 * Declares private data members
 */
public class SingleArtistInfo {

	private String artist; 
	private int noOfListeners; 
	private int playCount; 
	private String biography; 
	private String imageUrl;
	private JsonArray topTracks;
	
	/**
	 * Initialises private data members
	 * @param artistName
	 * @param listeners
	 * @param count
	 * @param bio
	 * @param image
	 * @param topTracks
	 */
	public SingleArtistInfo(String artistName, int listeners, int count, String bio, String image, JsonArray topTracks) {
		this.artist = artistName; 
		this.noOfListeners = listeners; 
		this.playCount = count; 
		this.biography = bio;
		this.imageUrl = image;
		this.topTracks = topTracks;
	}
	
	/** 
	 * Returns the artist name 
	 * @return
	 */
	public String getArtist() {
		return this.artist;
	}
	
	/** 
	 * Returns the number of listners 
	 * @return
	 */
	public int getListeners() {
		return this.noOfListeners;
	}
	
	/**
	 * Returns the play count
	 * @return
	 */
	public int getPlayCount() {
		return this.playCount;
	}
	
	/**
	 * Returns the biography
	 * @return
	 */
	public String getBio() {
		return this.biography;
	}
	
	/**
	 * Returns the artist image
	 * @return
	 */
	public String getImage() {
		return this.imageUrl;
	}
	
	/**
	 * To String method
	 */
	public String toString() {
		return "Artist: " + this.artist + " \nListeners: " + this.noOfListeners + " \nPlayCount: " + this.playCount + "\n";
	}
	
	/**
	 * Returns a JsonArray of all top tracks
	 * @return
	 */
	public JsonArray topTracks() {
		
		String topTracks = this.topTracks.toString();
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(topTracks);
		JsonArray newArray = element.getAsJsonArray();
		
		return newArray;
	}

	/**
	 * Returns a JsonObject of all information related to the artist
	 * @return
	 */
	public JsonObject toJson() {
		
		JsonObject object = new JsonObject();
		object.addProperty("artist", getArtist());
		object.addProperty("listeners", getListeners());
		object.addProperty("playCount", getPlayCount());
		object.addProperty("bio", getBio());
		object.addProperty("image", getImage());
		object.add("topTracks", topTracks()); 
		return object;
	}
}
