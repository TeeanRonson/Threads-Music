package songfinder;

import java.util.ArrayList;
import java.util.TreeSet;

/** 
 * Declares private data members 
 * @author Rong
 */
public class SingleSongInfo {
	private String artist; 
	private String title; 
	private String trackId;
	private TreeSet<String> tagList;
	 
	/**
	 * Constructor that takes in four inputs: 
	 * 3 String objects and a ArrayList<String> object
	 * @param artist
	 * @param title
	 * @param trackId
	 * @param tagList
	 */
	public SingleSongInfo(String artist, String title, String trackId, TreeSet<String> tagList) {
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
	
	public TreeSet<String> getTagList() {
		return this.tagList;
	}
	
	public String toString() {
		return "Artist: " + this.artist + " \nTitle: " + this.title + " \nTrackId: " + this.trackId + " ";
	}
}
