package songfinder;

import java.util.ArrayList;
import java.util.TreeSet;

public class SingleSongInfo {
	private String artist; 
	private String title; 
	private String trackId;
	private ArrayList<String> tagList;
	 
	public SingleSongInfo(String artist, String title, String trackId, ArrayList<String> tagList) {
		this.artist = artist; 
		this.title = title;
		this.trackId = trackId;
		this.tagList = tagList;
		
	}

	public String getArtist() {
		return this.artist;
	}
	
	public String getTitle() {
		return this.title;
	}

	public String getTrackId() {
		return this.trackId;
	}
	
	public ArrayList<String> getTagList() {
		return this.tagList;
	}
	
	public String toString() {
		return "Artist: " + this.artist + " \nTitle: " + this.title + " \nTrackId: " + this.trackId + " ";
	}
	
	



}



