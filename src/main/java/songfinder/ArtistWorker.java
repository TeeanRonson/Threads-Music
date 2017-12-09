package songfinder;

import java.io.FileReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Path;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import BaseObjects.SingleArtistInfo;
import Libraries.MyArtistLibrary;
import Sockets.HttpFetcher;

/**
 * Worker class that implements Runnable
 * Each new Worker object will be passed into 
 * the WorkQueue method 'execute'.
 * 
 * Private data members 
 * @author Rong
 *
 */
public class ArtistWorker implements Runnable {
	
	private MyArtistLibrary addToArtiLibrary;
	private Path p;

	/**
	 * Initialises two private data members
	 * to be the values passed into the constructor 
	 * @param addTolibrary
	 * @param p
	 */
	public ArtistWorker(MyArtistLibrary addToALibrary, Path p) {
		this.addToArtiLibrary = addToALibrary;
		this.p = p;
		
	}
	
	/**Run method of the Worker class
	 * Processes only .json files
	 * Reads in data from the .json files and returns 
	 * a SingleArtistInfo object with the extracted  
	 * information from the Last.FM API
	 * 
	 * Adds the valid SingleArtistInfo object to 
	 * MyArtistLibrary class
	 * @param p
	 * @return
	 */
	@Override
	public void run() {
		
		SingleArtistInfo sai = null;
		
		if (p.toString().toLowerCase().endsWith(".json")) {
			
			try (FileReader fr = new FileReader(p.toFile().getAbsolutePath())) {
				
				JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(fr);
				JsonObject obj = element.getAsJsonObject();
			
				String artistName = obj.get("artist").getAsString();
				
				String artistInfo = "";
				String topTracksInfo = "";
				String encodedArtist = URLEncoder.encode(artistName, "UTF-8");
				String host = "ws.audioscrobbler.com";
				String APIKEY = "198f79089ba38013804c115cdebcc857";
				String artistInfoPath = "/2.0?artist=" + encodedArtist + "&api_key=" + APIKEY + "&method=artist.getInfo&format=json";
				String topTracksPath = "/2.0?artist=" + encodedArtist + "&api_key=" + APIKEY + "&method=artist.gettoptracks&format=json";
				
				artistInfo = HttpFetcher.download(host, artistInfoPath);
				topTracksInfo = HttpFetcher.download(host, topTracksPath);
				
				JsonArray tracks = null;
				
				if (topTracksInfo.contains("HTTP/1.1 200 OK")) {
					int start = topTracksInfo.indexOf("\n\n");
					topTracksInfo = topTracksInfo.substring(start + 2);
					
					JsonParser parser1 = new JsonParser();
					JsonElement element1 = parser1.parse(topTracksInfo);	
					JsonObject newObject = element1.getAsJsonObject();
					
					if (newObject.get("toptracks") != null) {
						JsonObject topTracks = newObject.get("toptracks").getAsJsonObject();
						tracks = topTracks.get("track").getAsJsonArray();
					}
				}
				
				Integer listeners = null;
				Integer playCount = null; 
				String content = null; 
				String imageUrl = null;
				
				if (artistInfo.contains("HTTP/1.1 200 OK")) {
					int start = artistInfo.indexOf("\n\n");
					artistInfo = artistInfo.substring(start + 2);
					
					JsonParser parser2 = new JsonParser();
					JsonElement element2 = parser2.parse(artistInfo);	
					JsonObject newObject = element2.getAsJsonObject();
					
					if (newObject.get("artist") != null) {
						JsonObject artistObject = newObject.get("artist").getAsJsonObject();
						JsonObject stats = artistObject.get("stats").getAsJsonObject();
						JsonObject bio = artistObject.get("bio").getAsJsonObject();
						JsonArray image = artistObject.get("image").getAsJsonArray();
						
						JsonObject imageSize = (JsonObject) image.get(4);
						
						listeners = stats.get("listeners").getAsInt();
						playCount = stats.get("playcount").getAsInt();
						content = bio.get("content").getAsString();
						imageUrl = imageSize.get("#text").getAsString();	
					}
				}	
				
				if (listeners != null && playCount != null && content != null && imageUrl != null && tracks != null) {
					sai = new SingleArtistInfo(artistName, listeners, playCount, content, imageUrl, tracks);
					this.addToArtiLibrary.addNewArtist(sai);
				}	
				
				
			} catch (IOException e) {
				e.getMessage();
				System.out.println("Can't find file");
			} 
		}		
	}
}
