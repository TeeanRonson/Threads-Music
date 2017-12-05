package Sockets;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Fetcher {
	
	
	public static void main(String[] args) {		
		
		String APIKEY = "198f79089ba38013804c115cdebcc857";		
		String artist = "Madonna";
		String page = HttpFetcher.download("ws.audioscrobbler.com", "/2.0?artist=" + artist + 
						"&api_key=" + APIKEY + 
						"&method=artist.gettoptracks&format=json");
		
		System.out.println(page);
		
		if (page.contains("HTTP/1.1 200 OK")) {
			int start = page.indexOf("\n\n");
			page = page.substring(start + 2);
			
			System.out.println(page);
			
			JsonParser parser1 = new JsonParser();
			JsonElement element1 = parser1.parse(page);
			JsonObject newObject = element1.getAsJsonObject();
//			
			JsonObject topTracks = newObject.get("toptracks").getAsJsonObject();
			JsonArray tracks = topTracks.get("track").getAsJsonArray();
			
			
//			
//			JsonArray nameWithLink = artistObject.get("image").getAsJsonArray();
//			JsonObject bio = artistObject.get("bio").getAsJsonObject();
//			JsonObject internalImage = (JsonObject) image.get(4);
//			
//			int listeners = stats.get("listeners").getAsInt();
//			int playCount = stats.get("playcount").getAsInt();
//			String content = bio.get("content").getAsString();
//			String text = internalImage.get("#text").getAsString();
//			
//			
//			System.out.println("Listeners " + listeners);
//			System.out.println("Playcount " + playCount);
//			System.out.println(text);
//			System.out.println("Content:  " + content);
//		
		}
	}

}
