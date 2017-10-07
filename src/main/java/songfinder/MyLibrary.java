package songfinder;

import java.nio.file.Path;
import java.util.Comparator;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Collections;

public class MyLibrary {
	private ArrayList<SingleSongInfo> byArtist;	
	private ArrayList <SingleSongInfo> byTitle;
	private TreeMap<String, ArrayList<String>> byTag;
	
	public MyLibrary() {
		byArtist = new ArrayList<SingleSongInfo>();
		byTitle = new ArrayList<SingleSongInfo>();
	}
	
	public void addSongInfo(SingleSongInfo object) {
		
		byArtist.add(object);
		
		Collections.sort(byArtist, new Comparator<SingleSongInfo>() {

			@Override
			public int compare(SingleSongInfo o1, SingleSongInfo o2) {
				
				int result0 = o1.getArtist().compareTo(o2.getArtist());
				int result1 = o1.getTitle().compareTo(o2.getTitle());
				
				if (result0 == 0 && result1 == 0) {
					return o1.getTrackId().compareTo(o2.getTrackId());
				} 
				else if (result0 == 0) {
					return result1;
				} else {
					return result0;
				}
			}	
		});
	
		byTitle.add(object);
		
		Collections.sort(byTitle, new Comparator<SingleSongInfo>() {
			
			@Override
			public int compare(SingleSongInfo o1, SingleSongInfo o2) {
					
				int result0 = o1.getTitle().compareTo(o2.getTitle());
				int result1 = o1.getArtist().compareTo(o2.getArtist());
					
				if (result0 == 0 && result1 == 0) { 
					return o1.getTrackId().compareTo(o2.getTrackId()); 
				} 
				else if (result0 == 0) {
					return result1;
				} else {
					return result0;
				}
			}	
		});
		
		String tag = object.getTag();
		if (!this.byTag.containsKey(tag)) {
			
			ArrayList<String> ids = new ArrayList<String>();
			ids.add(object.getTag());
			byTag.put(object.getTag(), ids);
			
		} else {
			
			ArrayList<String> ids = this.byTag.get(tag);
			ids.add(object.getTag());
		}
	}
		
	public boolean saveToFile(String output) {
		
		Path outpath = Paths.get(output);
		outpath.getParent().toFile().mkdirs();
		
		
		
		
		return true;
	}

}
