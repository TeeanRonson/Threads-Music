package songfinder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

public class MyLibrary {
	private TreeMap <String, ArrayList<SingleSongInfo>> byArtist;	
	private TreeMap <String, SingleSongInfo> byTitle;
	
	public MyLibrary() {
		byArtist = new TreeMap<String, ArrayList<SingleSongInfo>>();
		byTitle = new TreeMap<String, SingleSongInfo>();
		
	}
	
	public void addSongInfo(SingleSongInfo object) {
		
		String artist = object.getArtist();
		if (byArtist.containsKey(object.getArtist())) {
			
			ArrayList<SingleSongInfo> songs = this.byArtist.get(artist); 
			songs.add(object);
			
		} else {
			
			ArrayList<SingleSongInfo> songs = new ArrayList<SingleSongInfo>();
			songs.add(object);
		}
	}
	
	public boolean saveToFile(String output) {
		
		Path outpath = Paths.get(output);
		outpath.getParent().toFile().mkdirs();
		
		
		
		
		return true;
	}

}
