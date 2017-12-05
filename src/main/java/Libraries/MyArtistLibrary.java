package Libraries;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import BaseObjects.SingleArtistInfo;
import Generics.ReentrantLock;
import Utilities.ByPlayCountSorter;

public class MyArtistLibrary {
	
	private TreeMap<String, SingleArtistInfo> artistInfo;
	private TreeSet<SingleArtistInfo> artistInfoByPlayCount;
	private ReentrantLock rwl;
	private ByPlayCountSorter bpcs;
	
	public MyArtistLibrary() {
		this.bpcs = new ByPlayCountSorter();
		this.rwl = new ReentrantLock();
		this.artistInfo = new TreeMap<String, SingleArtistInfo>();
		this.artistInfoByPlayCount = new TreeSet<SingleArtistInfo>(bpcs);
	}
	
	
	public void addNewArtist(SingleArtistInfo object) {
		this.rwl.lockWrite();
		addArtistInfo(object);
		addByPlayCount(object);
		this.rwl.unlockWrite();	
	}
	
	private void addArtistInfo(SingleArtistInfo object) {
		
		if (!this.artistInfo.containsKey(object.getArtist())) {
			this.artistInfo.put(object.getArtist(), object);
		}
	}
	
		
	private void addByPlayCount(SingleArtistInfo object) {
			
		this.artistInfoByPlayCount.add(object);
	}
	
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
