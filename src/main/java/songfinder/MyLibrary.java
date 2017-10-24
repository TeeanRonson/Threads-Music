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
 * byArtist as a TreeMap that stores artist's as keys and SingleSongInfo object as values
 * byTitle as a TreeMap that stores title's as keys and SingleSongInfo object as values
 * byTag as a TreeMap to that stores tags as keys, and track id's as values 
 * @author Rong
 */
public class MyLibrary {
	
//TODO: nice design overall. I do recommend that you use Maps instead of List data structures.
//This will make it easier to access data in later projects.
//I also recommend you use a sorted Map so that you do not have to do a sort each time.
//The sorted data structure will insert in O(log n) whereas a full sort is an nlogn operation.	
	private TreeMap<String, TreeSet<SingleSongInfo>> byArtist;	
	private TreeMap<String, TreeSet<SingleSongInfo>> byTitle;
	private TreeMap<String, TreeSet<String>> byTag;
	private TreeMap<String, SingleSongInfo> findTitle;
	
	/**
	 * Constructor takes no inputs
	 * Initialises private data members
	 */
	public MyLibrary() {
		this.byArtist = new TreeMap<String, TreeSet<SingleSongInfo>>();
		this.byTitle = new TreeMap<String, TreeSet<SingleSongInfo>>();
		this.byTag = new TreeMap<String, TreeSet<String>>();
		this.findTitle = new TreeMap<String, SingleSongInfo>();
	}
	
	/**
	 * Public method takes as input a SingleSongInfo object 
	 * and adds the object to the ByArtist, ByTitle, ByTag  
	 * and findTitle data structures by calling the 
	 * respective private methods
	 * @param object
	 */
	public void addNewSong(SingleSongInfo object) { 
		addByArtist(object);
		addByTitle(object);
		addByTag(object);
		findTitle(object);
	}
	
	/**
	 * Private method takes as input a SingleSongInfo object
	 * adds the object to the TreeMap data structure and sorts
	 * by track id's
	 * @param object
	 */
	private void findTitle(SingleSongInfo object) {
		
		if (!this.findTitle.containsKey(object.getTrackId())) {
			this.findTitle.put(object.getTrackId(), object);
		}
	}
	
	/**
	 * Private method takes as input a SingleSongInfo object. 
	 * Takes artist as key and SingleSongInfo object as values
	 * if the key already exists, it adds the object to the existing TreeSet 
	 * if the key doesn't exist, it adds the object to a newly created TreeSet
	 * @param object
	 */
	private void addByArtist(SingleSongInfo object) {
		
		ByArtistSorter bas = new ByArtistSorter();
		if (!this.byArtist.containsKey(object.getArtist())) {
			TreeSet<SingleSongInfo> list = new TreeSet<SingleSongInfo>(bas);
			list.add(object);
			this.byArtist.put(object.getArtist(), list);
			
		} else { 
			
			TreeSet<SingleSongInfo> list = this.byArtist.get(object.getArtist());
			list.add(object);
		}
	}
	
	/**
	 * Private method takes as input a SingleSongInfo object. 
	 * Takes title as key and SingleSongInfo object as values
	 * if the key already exists, it adds the object to the existing TreeSet 
	 * if the key doesn't exist, it adds the object to a newly created TreeSet
	 * @param object
	 */
	private void addByTitle(SingleSongInfo object) {
		ByTitleSorter bts = new ByTitleSorter();
		if (!this.byTitle.containsKey(object.getTitle())) {
			TreeSet<SingleSongInfo> list = new TreeSet<SingleSongInfo>(bts);
			list.add(object);
			this.byTitle.put(object.getTitle(), list);
			
		} else { 
			
			TreeSet<SingleSongInfo> list = this.byTitle.get(object.getTitle());
			list.add(object);
		}
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
		
		TreeSet<String> listOfTags = object.getTagList();
		for (String x: listOfTags) {
			if (x != null) {
				if (!this.byTag.containsKey(x)) {
					TreeSet<String> ids = new TreeSet<String>();
					ids.add(object.getTrackId());
					this.byTag.put(x, ids);
				
				} else {
			
					TreeSet<String> ids = this.byTag.get(x);
					ids.add(object.getTrackId());
				}
			}	
		}
	}
	
	/**
	 * Public method takes as input the output directory
	 * and the 'order' variable which considers how the user
	 *  would like the song data to be sorted. 
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
		TreeMap<String, TreeSet<SingleSongInfo>> printer = new TreeMap<String, TreeSet<SingleSongInfo>>();
		
		try (BufferedWriter out = Files.newBufferedWriter(outPath)) {

			if (order.equals(artist)) {
				printer = this.byArtist;
			} else if (order.equals(title)){ 
				printer = this.byTitle;
			}	
			
			// byArtist and byTitle is already sorted accordingly. 
			// Loop through the key set which is in ascending order
			// List out data in the format "Artist - Title" using the SingleSongInfo object's methods
			if (!order.equals(tag)) {
				Set<String> listOut = printer.keySet();
				for (String x: listOut) {
					TreeSet<SingleSongInfo> list = printer.get(x);
					for (SingleSongInfo y: list) {
						out.write(y.getArtist() + " - " + y.getTitle() + "\n");
					}
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
