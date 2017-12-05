package BaseObjects;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SingleArtistInfo {

	private String artist; 
	private int noOfListeners; 
	private int playCount; 
	private String biography; 
	private String imageUrl;
	private JsonArray topTracks;
	
	public SingleArtistInfo(String artistName, int listeners, int count, String bio, String image, JsonArray topTracks) {
		this.artist = artistName; 
		this.noOfListeners = listeners; 
		this.playCount = count; 
		this.biography = bio;
		this.imageUrl = image;
		this.topTracks = topTracks;
	}
	
	
	public String getArtist() {
		return this.artist;
	}
	
	public int getListeners() {
		return this.noOfListeners;
	}
	
	public int getPlayCount() {
		return this.playCount;
	}
	
	public String getBio() {
		return this.biography;
	}
	
	public String getImage() {
		return this.imageUrl;
	}
	
	public String toString() {
		return "Artist: " + this.artist + " \nListeners: " + this.noOfListeners + " \nPlayCount: " + this.playCount + "\n";
	}
	
	public JsonArray topTracks() {
		
		String topTracks = this.topTracks.toString();
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(topTracks);
		JsonArray newArray = element.getAsJsonArray();
		
		return newArray;
	}
	
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
