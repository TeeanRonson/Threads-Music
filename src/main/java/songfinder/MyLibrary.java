package songfinder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Set;
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
		this.byArtist = new ArrayList<SingleSongInfo>();
		this.byTitle = new ArrayList<SingleSongInfo>();
		this.byTag = new TreeMap<String, ArrayList<String>>();
	}
	
	public void addNewSong(SingleSongInfo object) { 
		addByArtist(object);
		addByTitle(object);
		addByTag(object);
		
		System.out.println(byArtist.get(0).getArtist());
		
	}	
	
	private void addByArtist(SingleSongInfo object) {
		
		this.byArtist.add(object);
		
		Collections.sort(this.byArtist, new Comparator<SingleSongInfo>() {

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
	}
	
	private void addByTitle(SingleSongInfo object) {
		
//		this.byTitle.add(object);
	
		Collections.sort(this.byTitle, new Comparator<SingleSongInfo>() {
			
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
	}	
	
		// Add to TreeMap that will have the tags as keys and trackid's as values
		
	private void addByTag(SingleSongInfo object) {
		
		ArrayList<String> listOfTags = object.getTagList();
		for (String x: listOfTags) {
			if (x != null) {
				if (!this.byTag.containsKey(x)) {
					ArrayList<String> ids = new ArrayList<String>();
					ids.add(object.getTrackId());
					this.byTag.put(x, ids);
				
				} else {
			
					ArrayList<String> ids = this.byTag.get(x);
					ids.add(object.getTrackId());
				}
			}	
		}
		
	}
	
	public boolean artistAndTitleOutput(String output, String order) {
	
		Path outPath = Paths.get(output);
		outPath.getParent().toFile().mkdirs();
		
		String artist = "artist";
		String title = "title";
		String tag = "tag";
		boolean successful = false;
		ArrayList<SingleSongInfo> printer = new ArrayList<SingleSongInfo>();
		
		try(BufferedWriter out = Files.newBufferedWriter(outPath)) {

			if (order.equals(artist)) {
				printer = this.byArtist;
			} else if (order.equals(title)){ 
				printer = this.byTitle;
			}	
			
			if (!order.equals(tag)) {
				for (int i = 0; i < printer.size(); i++) {
					out.write(printer.get(i).getArtist() + " - " + printer.get(i).getTitle() + "\n");
				}
			} else {
				
				Set<String> tags = byTag.keySet();
				for (String x: tags) {
					out.write(x + ": ");
					for(String id: byTag.get(x)) {
						out.write(id + " ");
					}
					out.write("\n");
				}
			}
			successful = true;
			
		} catch (IOException e) {
			e.getMessage();
			System.out.println(e);		
			return successful;
		}
		return successful;
	}
	
	public int checkSize() {
		return this.byArtist.size(); 
	}

}
