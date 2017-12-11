package Libraries;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;
import java.util.TreeSet;

import Generics.ReentrantLock;
import Utilities.ByArtistSorter;
import Utilities.ByPlayCountSorter;
import Utilities.ByTitleSorter;

import java.util.Collections;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import BaseObjects.SingleArtistInfo;
import BaseObjects.SingleSongInfo;

/**
 * Constructor declaring private data members
 * byArtist as a TreeMap that stores artist's as keys and SingleSongInfo object as values
 * byTitle as a TreeMap that stores title's as keys and SingleSongInfo object as values
 * byTag as a TreeMap to that stores tags as keys, and track id's as values 
 * @author Rong
 */
public class MyMusicLibrary {
	private TreeMap<String, TreeSet<SingleSongInfo>> byArtist;	
	private TreeMap<String, TreeSet<SingleSongInfo>> byTitle;
	private TreeMap<String, TreeSet<String>> byTagToTrackId;
	private TreeMap<String, SingleSongInfo> byTrackId;
	private HashMap<String, String> caseArtist;
	private HashMap<String, String> caseTitle;
	private HashMap<String, String> caseTag;
	private ReentrantLock rwl;
	private ByArtistSorter sortArtist;
	private ByTitleSorter sortTitle;
	
	/**
	 * Constructor takes no inputs
	 * Initialises private data members
	 */
	public MyMusicLibrary() {
		this.byArtist = new TreeMap<String, TreeSet<SingleSongInfo>>();
		this.byTitle = new TreeMap<String, TreeSet<SingleSongInfo>>();
		this.byTagToTrackId = new TreeMap<String, TreeSet<String>>();
		this.byTrackId = new TreeMap<String, SingleSongInfo>();
		this.caseArtist = new HashMap<String, String>();
		this.caseTitle = new HashMap<String, String>();
		this.caseTag = new HashMap<String, String>();
		rwl = new ReentrantLock();
		sortArtist = new ByArtistSorter();
		sortTitle = new ByTitleSorter();
	}	
	
	/**
	 * Public method takes as input a SingleSongInfo object 
	 * and adds the object to the ByArtist, ByTitle, ByTag  
	 * and findTitle data structures by calling the 
	 * respective private methods
	 * @param object
	 */
	public void addNewSong(SingleSongInfo object) {
		this.rwl.lockWrite();
		addByArtist(object);
		addByTitle(object);
		addByTagToTrackId(object);
		addByTrackId(object);
		addArtistName(object);
		addTitleName(object);
		addTagName();
		this.rwl.unlockWrite();	
	}
	
	/**
	 * Project3 additional private methods
	 */
	private void addArtistName(SingleSongInfo object) {
		
		this.caseArtist.put(object.getArtist().toLowerCase(), object.getArtist());
	}
	
	private void addTitleName(SingleSongInfo object) {
		
		this.caseTitle.put(object.getTitle().toLowerCase(), object.getTitle());
	}
	
	private void addTagName() {
		
		for (String x: this.byTagToTrackId.keySet()) {
			this.caseTag.put(x.toLowerCase(), x);
		}
	}
	
	/**
	 * Private method takes as input a SingleSongInfo object
	 * adds the object to the TreeMap data structure and sorts
	 * by track id's
	 * @param object
	 */
	private void addByTrackId(SingleSongInfo object) {
		
		if (!this.byTrackId.containsKey(object.getTrackId())) {
			this.byTrackId.put(object.getTrackId(), object);
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
	private void addByTagToTrackId(SingleSongInfo object) {
		
		TreeSet<String> listOfTags = object.getTagList();
		for (String x: listOfTags) {
			if (x != null) {
				if (!this.byTagToTrackId.containsKey(x)) {
					TreeSet<String> ids = new TreeSet<String>();
					ids.add(object.getTrackId());
					this.byTagToTrackId.put(x, ids);
				
				} else {
			
					TreeSet<String> ids = this.byTagToTrackId.get(x);
					ids.add(object.getTrackId());
				}
			}	
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
		
		if (!this.byArtist.containsKey(object.getArtist())) {
			TreeSet<SingleSongInfo> list = new TreeSet<SingleSongInfo>(sortArtist);
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

		if (!this.byTitle.containsKey(object.getTitle())) {
			TreeSet<SingleSongInfo> list = new TreeSet<SingleSongInfo>(sortTitle);
			list.add(object);
			this.byTitle.put(object.getTitle(), list);
			
		} else { 
			
			TreeSet<SingleSongInfo> list = this.byTitle.get(object.getTitle());
			list.add(object);
		}
	}	
	
	/**
	 * Public method that calls a helper method to 
	 * search by artist or title and returns a 
	 * JsobObject in the specific format
	 * @param item
	 * @param type
	 * @return
	 */
	public JsonObject searchHelper(String item, String type) {
		
			try { 
				rwl.lockRead();
			
				TreeMap<String, TreeSet<SingleSongInfo>> data = new TreeMap<String, TreeSet<SingleSongInfo>>();
				
				if (type.equals("artist")) {
					data = this.byArtist;
				} else {
					data = this.byTitle;
				}
		
				return searchArtistOrTitle(item, data, type);
			} finally { 
				rwl.unlockRead();
			}
	}		
	
	/**
	 * Private method creates JsonObjects and 
	 * adds each new object into a JsonArray
	 * 
	 * Takes as parameter a TreeSet of track id's
	 * @param result
	 * @return
	 */
	private JsonArray createJson(TreeSet<String> result) {
		
		JsonArray similarsList = new JsonArray();
		
		for (String z: result) {
			JsonObject similarObject = new JsonObject();
			if (byTrackId.get(z) != null) {
				similarObject.addProperty("artist", byTrackId.get(z).getArtist()); 
				similarObject.addProperty("trackId", z);
				similarObject.addProperty("title", byTrackId.get(z).getTitle());
			}
			if (similarObject.size() != 0) {
				similarsList.add(similarObject);
			}	
		}
		
		return similarsList;
	}
	
	/**
	 * Private method takes as input a given item 
	 * which is either an artist or title name 
	 * and returns a JsonObject with the item and 
	 * a list of similar songs related to that item
	 * @param item
	 * @return
	 */
	private JsonObject searchArtistOrTitle(String item, TreeMap<String, TreeSet<SingleSongInfo>> data, String type) {
		
		JsonObject object = new JsonObject();
		JsonArray similarsList = new JsonArray();
		TreeSet<String> result = new TreeSet<String>();	
		
		try { 
			this.rwl.lockRead();
			
			
			if (!data.containsKey(item)) {
				 if (type.equals("artist")) {
					object.addProperty("artist", item);
					object.add("similars", similarsList);
				} else {
					object.addProperty("title", item);
					object.add("similars", similarsList);
				}
				 
			} else { 
				TreeSet<SingleSongInfo> songs = data.get(item); 
				for (SingleSongInfo x: songs) {		
					if (x != null) {
						TreeSet<String> trackIds = x.getSimilarSongs();
						for (String y: trackIds) {		
							result.add(y);
						}	
					}	
				}
				
				similarsList = createJson(result);
				
				if (this.byArtist.containsKey(item)) {
					object.addProperty("artist", item);
					object.add("similars", similarsList);
				} else if (this.byTitle.containsKey(item)) {
					object.addProperty("title", item);
					object.add("similars", similarsList);
				}
			}	
			return object;	
		} finally { 
			this.rwl.unlockRead();
		}
	}	
	
	/**
	 * Public method takes as input a given tag and
	 * returns a JsonObject with the tag name and
	 * a list of similar songs related to that tag
	 * @param tag
	 * @return
	 */
	public JsonObject searchByTag(String tag) {
		
		JsonObject tagObject = new JsonObject();
		JsonArray similarsList = new JsonArray();
		
		try {
			
			this.rwl.lockRead();
			
			if (this.byTagToTrackId.get(tag) == null) {
				System.out.println("No songs related to tag");
				
			} else { 
				
				for (String x: byTagToTrackId.get(tag)) { 
					JsonObject similarObject = new JsonObject();
					if (this.byTrackId.get(x) != null) {
						similarObject.addProperty("artist", byTrackId.get(x).getArtist());
						similarObject.addProperty("title", byTrackId.get(x).getTitle());
						similarObject.addProperty("trackId", byTrackId.get(x).getTrackId());
					}
					
					if (similarObject.size() != 0) {
						similarsList.add(similarObject);
					}	
				}
				tagObject.addProperty("tag", tag);
				tagObject.add("similars", similarsList);
			}
			return tagObject;
		} finally {
			this.rwl.unlockRead();
		}	
	}
	
	
	
	/**
	 * Method that solves case sensitivity for all artists queried by the user.
	 * @param artist
	 * @param type
	 * @return
	 */
	public JsonObject caseCheckArtist(String artist, String type) {

		try { 
			rwl.lockRead();
		
			JsonObject result = new JsonObject();
			if (this.caseArtist.containsKey(artist)) {
				result = searchHelper(this.caseArtist.get(artist), type);
			} else { 
				result = partialSearch(artist, type);
			}
			return result;
		} finally { 
			rwl.unlockRead();
		}
	}	
	
	/**
	 * Method that solves case sensitivity for all titles queried by the user. 
	 * @param title
	 * @param type
	 * @return
	 */
	public JsonObject caseCheckTitle(String title, String type) {
		
		try { 
			rwl.lockRead();
		
			JsonObject result = new JsonObject();
			if (this.caseTitle.containsKey(title)) {
				result = searchHelper(this.caseTitle.get(title), type);
			} else { 
				result = partialSearch(title, type);
			}
			
			return result;
		} finally { 
			rwl.unlockRead();
		}
	}
	
	/**
	 * Method that solves case sensitivity for all tags queried by the user.  
	 * @param tag
	 * @return
	 */
	public JsonObject caseCheckTag(String tag) {
		
		try { 
			rwl.lockRead();
		
			JsonObject result = new JsonObject();
			
			if (this.caseTag.containsKey(tag)) {
				result = searchByTag(this.caseTag.get(tag));
			} else { 
				result = partialSearch(tag, "tag");
			}
			
			return result;
		} finally { 
			rwl.unlockRead();
		}
	}	

	/** 
	 * Partial search method. Allows the user to enter only part of an artist, title, 
	 * or tag and return relevant results.
	 * @param item
	 * @param type
	 * @return
	 */
	private JsonObject partialSearch(String item, String type) {
		
		JsonObject singleObject = new JsonObject();
		JsonObject returnObject = new JsonObject();
		JsonArray similarsArray = new JsonArray();
		JsonArray result = new JsonArray();
		
		TreeSet<String> related = partialSearchHelper(item, type);
		
		if (type.equals("tag")) {
			for (String x: related) {
				singleObject = searchByTag(x);
				similarsArray = singleObject.get("similars").getAsJsonArray();
				for (JsonElement y: similarsArray) {
					result.add(y);
				}
			}
			
		} else {
			
			for (String x: related) {
				singleObject = searchHelper(x, type);
				similarsArray = singleObject.get("similars").getAsJsonArray();
				for (JsonElement y: similarsArray) {
					result.add(y);
				}
			}	
			returnObject.add("similars", result);
		}	
		
		return returnObject;
	}
	
	/**
	 * Private method called by partialSearch to obtain 
	 * all items that contains elements of the query.
	 * @param item
	 * @param type
	 * @return
	 */
	private TreeSet<String> partialSearchHelper(String item, String type) {
		
		TreeSet<String> related = new TreeSet<String>();
		
		if (type.equals("artist")) {
			for (String x: this.caseArtist.keySet()) {
				if (x.contains(item)) {
					related.add(this.caseArtist.get(x));
				}
			}
		}
		
		else if (type.equals("title")) {
			for (String x: this.caseTitle.keySet()) {
				if (x.contains(item)) {
					related.add(this.caseTitle.get(x));
				}
			}
		}
		
		else {
			for (String x: this.caseTag.keySet()) {
				if (x.contains(item)) {
					related.add(this.caseTag.get(x));
				}
			}
		}
		return related;
	} 

	/**
	 * Public method called by Servlets to retrieve
	 * an artist name for a given trackId
	 * @param trackId
	 * @return
	 */
	public String getName(String trackId) {
		
		try { 
			rwl.lockRead();
		
			String name = this.byTrackId.get(trackId).getArtist();
			return name;
		} finally { 
			rwl.unlockRead();
		}
	}	
	
	/**
	 * Public method called by Servlets to retrieve
	 * a title name for a given trackId
	 * @param trackId
	 * @return
	 */
	public String getTitle(String trackId) {
		
		try {
			rwl.lockRead();
		
			String title = this.byTrackId.get(trackId).getTitle();
			return title;
		} finally { 
			rwl.unlockRead();
		}
	}
	
	/**
	 * Public method returns a JsonArray of JsonObjects
	 * which contains information on each SingleSongInfo
	 * object related to a given artist 
	 * @param artist
	 * @return
	 */
	public JsonArray getArtistSongs(String artist) {
		
		try { 
			rwl.lockRead();
	
			JsonArray result = new JsonArray();
			TreeSet<SingleSongInfo> songObjects = this.byArtist.get(artist);
			for (SingleSongInfo x: songObjects) {
				result.add(x.toJson());
			}
			return result;
		} finally { 
			rwl.unlockRead();
		}
	}
	
	/**
	 * Public method returns a JsonArray of JsonObjects
	 * which contains information on each SingleSongInfo
	 * object related to a given title
	 * @param title
	 * @return
	 */
	public JsonArray getTitleSongs(String title) {
			
			try { 
				rwl.lockRead();
				
				JsonArray result = new JsonArray();
				TreeSet<SingleSongInfo> list = this.byTitle.get(title);
				
				for (SingleSongInfo x: list) {
					result.add(x.toJson());
				}
				
				return result;
			} finally { 
				rwl.unlockRead();
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
		
		try {
			
			this.rwl.lockRead();
		
			try (BufferedWriter out = Files.newBufferedWriter(outPath)) {

				if (order.equals(artist)) {
					printer = this.byArtist;
				} else if (order.equals(title)){ 
					printer = this.byTitle;
				}	

				if (!order.equals(tag)) {
					Set<String> listOut = printer.keySet();
					for (String x: listOut) {
						TreeSet<SingleSongInfo> list = printer.get(x);
						for (SingleSongInfo y: list) {
							out.write(y.getArtist() + " - " + y.getTitle() + "\n");
						}
					}
				
				} else {
				
					Set<String> tags = byTagToTrackId.keySet();
					for (String x: tags) {
						out.write(x + ": ");
						for(String id: byTagToTrackId.get(x)) {
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
		} finally {
			this.rwl.unlockRead();
		}
	}
}
