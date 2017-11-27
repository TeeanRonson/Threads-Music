package songfinder;

import java.util.TreeMap;

import com.google.gson.JsonObject;

import Generics.ReentrantLock;

public class MyArtistLibrary {
	
	private TreeMap<String, SingleArtistInfo> artistInfo;
	private ReentrantLock rwl;
	
	public MyArtistLibrary() {
		this.artistInfo = new TreeMap<String, SingleArtistInfo>();
		this.rwl = new ReentrantLock();
	}
	
	
	public void addNewArtist(SingleArtistInfo object) {
		this.rwl.lockWrite();
		addArtistInfo(object);
		this.rwl.unlockWrite();	
	}
	
	private void addArtistInfo(SingleArtistInfo object) {
		
		if (!this.artistInfo.containsKey(object.getArtist())) {
			this.artistInfo.put(object.getArtist(), object);
		}
		
		System.out.println(artistInfo.keySet());
	}
	
}
