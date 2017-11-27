package songfinder;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

import Generics.ReentrantLock;
import Utilities.ByArtistSorter;
import Utilities.ByTitleSorter;

import java.util.Collections;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Constructor declaring private data members
 * byArtist as a TreeMap that stores artist's as keys and SingleSongInfo object as values
 * byTitle as a TreeMap that stores title's as keys and SingleSongInfo object as values
 * byTag as a TreeMap to that stores tags as keys, and track id's as values 
 * @author Rong
 */
public class MyLibrary {
	private TreeMap<String, TreeSet<SingleSongInfo>> byArtist;	
	private TreeMap<String, TreeSet<SingleSongInfo>> byTitle;
	private TreeMap<String, TreeSet<String>> byTagToTrackId;
	private TreeMap<String, SingleSongInfo> byTrackId;
	private ReentrantLock rwl;
	private ByArtistSorter sortArtist;
	private ByTitleSorter sortTitle;

	
	/**
	 * Constructor takes no inputs
	 * Initialises private data members
	 */
	public MyLibrary() {
		this.byArtist = new TreeMap<String, TreeSet<SingleSongInfo>>();
		this.byTitle = new TreeMap<String, TreeSet<SingleSongInfo>>();
		this.byTagToTrackId = new TreeMap<String, TreeSet<String>>();
		this.byTrackId = new TreeMap<String, SingleSongInfo>();
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
		this.rwl.unlockWrite();	
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
	 * Public method takes as input a given artist 
	 * and returns a JsonObject with the artist name
	 * and a list of similar songs related to that artist
	 * @param artist
	 * @return
	 */
	public JsonObject searchByArtist(String artist) {
		
		JsonArray similarsList = new JsonArray();
		JsonObject artistObject = new JsonObject();
		TreeSet<String> result = new TreeSet<String>();	
		
		try { 
			this.rwl.lockRead();
			
			if (this.byArtist.get(artist) == null) {
				artistObject.addProperty("artist", artist);
				artistObject.add("similars", similarsList);
				
			} else { 
			
				TreeSet<SingleSongInfo> songs = this.byArtist.get(artist); 
				for (SingleSongInfo x: songs) {		
					if (x != null) {
						TreeSet<String> trackIds = x.getSimilarSongs();
						for (String y: trackIds) {		
							result.add(y);
						}	
					}	
				}
				
				similarsList = createJson(result);
				artistObject.addProperty("artist", artist);
				artistObject.add("similars", similarsList);
			}
			return artistObject;	
		} finally { 
			this.rwl.unlockRead();
		}
	}	
	
	/**
	 * Public method takes as input a given title 
	 * and returns a JsonObject with the title name
	 * and a list of similar songs related to that title
	 * @param title
	 * @return
	 */
	public JsonObject searchByTitle(String title) {
		
		JsonArray similarsList = new JsonArray();
		JsonObject titleObject = new JsonObject();
		TreeSet<String> result = new TreeSet<String>();
		
		try { 
			
			this.rwl.lockRead();
				
			if (this.byTitle.get(title) == null) {
				titleObject.add("similars", similarsList);
				titleObject.addProperty("title", title);
				
			} else { 
				
				TreeSet<SingleSongInfo> songs = this.byTitle.get(title);
				for (SingleSongInfo x: songs) {
					TreeSet<String> trackIds = x.getSimilarSongs();
					for (String y: trackIds) {
							result.add(y);
					}	
				}

				similarsList = createJson(result);				
				titleObject.addProperty("title", title);
				titleObject.add("similars", similarsList);
			}
			return titleObject;
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
	 * Public method takes no input 
	 * and returns the byArtist data structure
	 * @return
	 */
	public TreeMap<String, TreeSet<SingleSongInfo>> getByArtist() {
		
		try { 
			this.rwl.lockRead();
			
			return this.byArtist;
		} finally { 
			this.rwl.unlockRead();
		}
	}
	
	/**
	 * Public method takes no input 
	 * and returns the byTitle data structure
	 * @return
	 */
	public TreeMap<String, TreeSet<SingleSongInfo>> getByTitle() {
		
		try { 
			this.rwl.lockRead();
			
			return this.byTitle;
		} finally { 
			this.rwl.unlockRead();
		}
	}
	
	/**
	 * Public method takes no input 
	 * and returns the byTagToTrackId data structure
	 * @return
	 */
	public TreeMap<String, TreeSet<String>> getByTagToTrackId() {
		
		try { 
			this.rwl.lockRead();
		
			return this.byTagToTrackId;
		} finally { 
			this.rwl.unlockRead();
		}
	}	
	
	/**
	 * Public method takes no input 
	 * and returns the byTrackId data structure
	 * @return
	 */
	public TreeMap<String, SingleSongInfo> getByTrackId() {
		
		try { 
			this.rwl.lockRead();

			return this.byTrackId;
		} finally { 
			this.rwl.unlockRead();
		}
	}	
	
	/**
	 * Private method takes as input an input file 
	 * with the list of artists, titles and tags 
	 * to search
	 * 
	 * Returns a JsonObject with JsonArray's of searched items
	 * @param inputFile
	 * @return
	 */
	private JsonObject search(String inputFile) {
		
		Path path = Paths.get(inputFile);
		JsonObject dummy = new JsonObject();
		JsonObject searched = new JsonObject();
		JsonArray artistArray = new JsonArray();
		JsonArray titleArray = new JsonArray();
		JsonArray tagArray = new JsonArray();
			
		if (!path.toString().toLowerCase().endsWith(".json")) {
			System.out.println("Not a json file");
			
		} else { 
				
			try (FileReader fr = new FileReader(path.toFile().getAbsolutePath())) {
				
				JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(fr);
				JsonObject obj = element.getAsJsonObject();
				
				JsonArray getArtist = obj.getAsJsonArray("searchByArtist");
				JsonArray getTitle = obj.getAsJsonArray("searchByTitle");
				JsonArray getTag = obj.getAsJsonArray("searchByTag");
				
				if (getArtist != null) {
					for (int i = 0; i < getArtist.size(); i++) {
						dummy = searchByArtist(getArtist.get(i).getAsString());	
						artistArray.add(dummy);
					}
				}	
				
				if (getTag != null) {
					for (int i = 0; i < getTag.size(); i++) {
						dummy = searchByTag(getTag.get(i).getAsString());
						tagArray.add(dummy);
					}
				}	
				
				if (getTitle != null) {
					for (int i = 0; i < getTitle.size(); i++) {
						dummy = searchByTitle(getTitle.get(i).getAsString());
						titleArray.add(dummy);
					}
				}	
				
				if (artistArray.size() != 0) {
					searched.add("searchByArtist", artistArray);
				}
				
				if (titleArray.size() != 0) {
					searched.add("searchByTitle", titleArray);
				}	
				
				if (tagArray.size() != 0) {
					searched.add("searchByTag", tagArray);
				}
				
			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		return searched;
		
	}
	
	
	/**
	 * Public method calls private methods to 
	 * help search items in input file
	 * and writes to the output file 
	 * @param inputFile
	 * @param outputFile
	 */
	public void searchResultsOutput(String inputFile, String outputFile) {
		
		if (inputFile != null && outputFile != null) {
		
			Path outPath = Paths.get(outputFile);
			outPath.getParent().toFile().mkdirs();
			
			try {
				
				this.rwl.lockRead();
			
				try (BufferedWriter out = Files.newBufferedWriter(outPath)) {
					
					out.write(search(inputFile).toString());
					
				} catch (IOException e) {
				e.getMessage();
				}
			} finally {
				this.rwl.unlockRead();
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
