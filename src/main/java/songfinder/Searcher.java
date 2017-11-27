package songfinder;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeSet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Generics.ReentrantLock;

public class Searcher {

	private MyLibrary library; // could it be that the library object here is not built?
	private ReentrantLock rwl;
	
	public Searcher() {
		 this.library = new MyLibrary();
		 this.rwl = new ReentrantLock();
		
	}
//
//	
//	private JsonArray createJson(TreeSet<String> result) {
//			
//			JsonArray similarsList = new JsonArray();
//			
//			for (String z: result) {
//				JsonObject similarObject = new JsonObject();
//				if (library.getByTrackId().get(z) != null) {
//					similarObject.addProperty("artist", library.getByTrackId().get(z).getArtist()); 
//					similarObject.addProperty("trackId", z);
//					similarObject.addProperty("title", library.getByTrackId().get(z).getTitle());
//				}
//				if (similarObject.size() != 0) {
//					similarsList.add(similarObject);
//				}	
//			}
//			
//			System.out.println("From searcher: " + library.getByTrackId().keySet());
//			return similarsList;
//		}
//
//	public JsonObject searchByArtist(String artist) {
//		
//		JsonArray similarsList = new JsonArray();
//		JsonObject artistObject = new JsonObject();
//		TreeSet<String> result = new TreeSet<String>();	
//		
//	
//		try { 
//			this.rwl.lockRead();
//			
//			if (library.getByArtist().get(artist) == null) {
//				artistObject.addProperty("artist", artist);
//				artistObject.add("similars", similarsList);
//				
//			} else { 
//			
//				TreeSet<SingleSongInfo> songs = library.getByArtist().get(artist); 
//				for (SingleSongInfo x: songs) {		
//					if (x != null) {
//						TreeSet<String> trackIds = x.getSimilarSongs();
//						for (String y: trackIds) {		
//							result.add(y);
//						}	
//					}	
//				}
//				
//				similarsList = createJson(result);
//				artistObject.addProperty("artist", artist);
//				artistObject.add("similars", similarsList);
//			}
//			
//			return artistObject;
//				
//		} finally { 
//			this.rwl.unlockRead();
//		}
//	}	
//
//
//	public JsonObject searchByTitle(String title) {
//		
//		JsonArray similarsList = new JsonArray();
//		JsonObject titleObject = new JsonObject();
//		TreeSet<String> result = new TreeSet<String>();
//		
//		try { 
//			
//			this.rwl.lockRead();
//				
//			if (this.library.getByTitle().get(title) == null) {
//				titleObject.add("similars", similarsList);
//				titleObject.addProperty("title", title);
//				
//			} else { 
//				
//				TreeSet<SingleSongInfo> songs = this.library.getByTitle().get(title);
//				for (SingleSongInfo x: songs) {
//					TreeSet<String> trackIds = x.getSimilarSongs();
//					for (String y: trackIds) {
//							result.add(y);
//					}	
//				}
//	
//				similarsList = createJson(result);				
//				titleObject.addProperty("title", title);
//				titleObject.add("similars", similarsList);
//			}
//				return titleObject;
//				
//			} finally { 
//				this.rwl.unlockRead();
//		}	
//	}
//
//	public JsonObject searchByTag(String tag) {
//		
//		JsonObject tagObject = new JsonObject();
//		JsonArray similarsList = new JsonArray();
//		
////		System.out.println(tag);
//		
//		try {
//			
//			this.rwl.lockRead();
//			
//			if (this.library.getByTagToTrackId().get(tag) == null) {
//				System.out.println("No songs related to tag");
//				
//			} else { 
//				
//				for (String x: library.getByTagToTrackId().get(tag)) { 
//					JsonObject similarObject = new JsonObject();
//					if (this.library.getByTrackId().get(x) != null) {
//						similarObject.addProperty("artist", library.getByTrackId().get(x).getArtist());
//						similarObject.addProperty("title", library.getByTrackId().get(x).getTitle());
//						similarObject.addProperty("trackId", library.getByTrackId().get(x).getTrackId());
//					}
//					
//					if (similarObject.size() != 0) {
//						similarsList.add(similarObject);
//					}	
//				}
//				
//				tagObject.addProperty("tag", tag);
//				tagObject.add("similars", similarsList);
//				
//			}
//			return tagObject;
//		} finally {
//			this.rwl.unlockRead();
//		}	
//	}
//
//
//	private JsonObject search(String inputFile) {
//	
//		Path path = Paths.get(inputFile);
//		JsonObject dummy = new JsonObject();
//		JsonObject searched = new JsonObject();
//		JsonArray artistArray = new JsonArray();
//		JsonArray titleArray = new JsonArray();
//		JsonArray tagArray = new JsonArray();
//			
//		if (!path.toString().toLowerCase().endsWith(".json")) {
//			System.out.println("Not a json file");
//			
//		} else { 
//				
//			try (FileReader fr = new FileReader(path.toFile().getAbsolutePath())) {
//				
//				JsonParser parser = new JsonParser();
//				JsonElement element = parser.parse(fr);
//				JsonObject obj = element.getAsJsonObject();
//				
//				JsonArray getArtist = obj.getAsJsonArray("searchByArtist");
//				JsonArray getTitle = obj.getAsJsonArray("searchByTitle");
//				JsonArray getTag = obj.getAsJsonArray("searchByTag");
//				
//				if (getArtist != null) {
//					for (int i = 0; i < getArtist.size(); i++) {
//						dummy = searchByArtist(getArtist.get(i).getAsString());	
//						artistArray.add(dummy);
//					}
//				}	
//				
//				if (getTag != null) {
//					for (int i = 0; i < getTag.size(); i++) {
//						dummy = searchByTag(getTag.get(i).getAsString());
//						tagArray.add(dummy);
//					}
//				}	
//				
//				if (getTitle != null) {
//					for (int i = 0; i < getTitle.size(); i++) {
//						dummy = searchByTitle(getTitle.get(i).getAsString());
//						titleArray.add(dummy);
//					}
//				}	
//				
//				if (artistArray.size() != 0) {
//					searched.add("searchByArtist", artistArray);
//				}
//				
//				if (titleArray.size() != 0) {
//					searched.add("searchByTitle", titleArray);
//				}	
//				
//				if (tagArray.size() != 0) {
//					searched.add("searchByTag", tagArray);
//				}
//				
//				
//				
//			} catch (FileNotFoundException e) {
//				System.out.println(e.getMessage());
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			
//		}
//		
//		return searched;
//	}
//	
//	public void searchResultsOutput(String inputFile, String outputFile) {
//		
//	
//		if (inputFile != null && outputFile != null) {
//		
//			Path outPath = Paths.get(outputFile);
//			outPath.getParent().toFile().mkdirs();
//			
//			try {
//				
//				this.rwl.lockRead();
//			
//				try (BufferedWriter out = Files.newBufferedWriter(outPath)) {
//					
//					out.write(search(inputFile).toString());
//					
//				} catch (IOException e) {
//				e.getMessage();
//				}
//			} finally {
//				this.rwl.unlockRead();
//			}
//		}	
//	}	
}