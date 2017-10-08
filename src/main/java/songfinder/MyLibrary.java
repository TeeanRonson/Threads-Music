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

/**
 * Constructor declaring private data members
 * byArtist as a ArrayList that is sorted by the artist 
 * byTitle as a ArrayList that is sorted by the title
 * byTag as a TreeMap to that stores tags as keys, and track id's as values 
 * @author Rong
 *
 */
public class MyLibrary {
	private ArrayList<SingleSongInfo> byArtist;	
	private ArrayList <SingleSongInfo> byTitle;
	private TreeMap<String, ArrayList<String>> byTag;
	
	/**
	 * Constructor takes no inputs
	 * Initialises private data members
	 */
	public MyLibrary() {
		this.byArtist = new ArrayList<SingleSongInfo>();
		this.byTitle = new ArrayList<SingleSongInfo>();
		this.byTag = new TreeMap<String, ArrayList<String>>();
	}
	
	/**
	 * Public method takes as input a SingleSongInfo object 
	 * and adds the object to the ByArtist, ByTitle and ByTag 
	 * data structures by calling the respective private 
	 * methods
	 * @param object
	 */
	public void addNewSong(SingleSongInfo object) { 
		addByArtist(object);
		addByTitle(object);
		addByTag(object);
		
		System.out.println(byArtist.get(0).getArtist());
		
	}	
	
	/**
	 * Private method takes as input a SingleSongInfo object
	 * adds the object to the data structure and sorts by Artist
	 * @param object
	 */
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
	
	/**
	 * Private method takes as input a SingleSongInfo object
	 * adds the object to the data structure and sorts by Title
	 * @param object
	 */
	private void addByTitle(SingleSongInfo object) {
		
		this.byTitle.add(object);
	
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
	
	/**
	 * Private method takes as input a SingleSongInfo object
	 * adds the object to the data structure.
	 * Uses a TreeMap so it automatically sorts the key values
	 * (tags)
	 * 
	 * If the tag already exists, it adds the trackId to the existing
	 * key. 
	 * If the tag doesn't exist, it creates a new ArrayList, uses the tag
	 * as a new key, and places the ArrayList as the value. 
	 * @param object
	 */	
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
	
	/**
	 * Public method takes as input the output directory
	 * and the order variable which considers how we would 
	 * like the song data to be sorted. 
	 * 
	 * Prints to file according to expected requirements
	 * @param output
	 * @param order
	 * @return
	 */
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
