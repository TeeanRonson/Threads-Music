package Libraries;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import BaseObjects.SingleArtistInfo;
import Generics.ReentrantLock;
import Utilities.ByPlayCountSorter;

/** 
 * Library that stores relevant artist information in 
 * several data structures
 * 
 * Declares private data members
 * @author Rong
 */
public class MyArtistLibrary {
	private TreeMap<String, SingleArtistInfo> artistInfo;
	private TreeSet<SingleArtistInfo> artistInfoByPlayCount;
	private ReentrantLock rwl;
	private ByPlayCountSorter bpcs;
	
	/**
	 * Initialises private data members
	 */
	public MyArtistLibrary() {
		this.bpcs = new ByPlayCountSorter();
		this.rwl = new ReentrantLock();
		this.artistInfo = new TreeMap<String, SingleArtistInfo>();
		this.artistInfoByPlayCount = new TreeSet<SingleArtistInfo>(bpcs);
	}
	
	/** 
	 * Adds a new artist object to several data structures
	 * @param object
	 */
	public void addNewArtist(SingleArtistInfo object) {
		this.rwl.lockWrite();
		addArtistInfo(object);
		addByPlayCount(object);
		this.rwl.unlockWrite();	
	}
	
	/**
	 * Saves the artist name as the key and the SingleArtistInfo object 
	 * as its value 
	 * @param object
	 */
	private void addArtistInfo(SingleArtistInfo object) {
		
		if (!this.artistInfo.containsKey(object.getArtist())) {
			this.artistInfo.put(object.getArtist(), object);
		}
	}
	
	/** 
	 * Saves the objects in order of play count
	 * @param object
	 */
	private void addByPlayCount(SingleArtistInfo object) {
			
		this.artistInfoByPlayCount.add(object);
	}
	
	/** 
	 * Returns a JsonObject of all information related to an artist
	 * @param artist
	 * @return
	 */
	public JsonObject getArtistInfo(String artist) {
		
		try { 
			rwl.lockRead();
			
			JsonObject result = new JsonObject();
			result = this.artistInfo.get(artist).toJson();
			
			return result;
		} finally { 
			rwl.unlockRead();
		}
	}
	
	/** 
	 * Returns top tracks by a given artist
	 * @param artist
	 * @return
	 */
	public JsonArray getTopTracks(String artist) {
		
		try { 
			rwl.lockRead();
			
			JsonArray result = new JsonArray();
			
			result = this.artistInfo.get(artist).topTracks();
			return result;
		} finally { 
			rwl.unlockRead();
		}
	}
	
	/** 
	 * Returns a JsonArray with all artist names
	 * @return
	 */
	public JsonArray displayArtists() { 
		
		try { 
			rwl.lockRead();
		
			JsonArray result = new JsonArray();
		
			for (String artistName: this.artistInfo.keySet()) {
				SingleArtistInfo s = this.artistInfo.get(artistName);
				result.add(s.toJson());
			}
		
			return result;	
		} finally { 
			rwl.unlockRead();;
		}
	}	
	
	/**
	 * Returns a JsonArray of all artists displayed by play count 
	 * @return
	 */
	public JsonArray displayByPlayCount() {
		
		try { 
			
			rwl.lockRead(); 
			
			JsonArray result = new JsonArray();
			for (SingleArtistInfo s: this.artistInfoByPlayCount) {
				result.add(s.toJson());
			}
		
			return result;
		} finally { 
			rwl.unlockRead();;
		}
	}
}
