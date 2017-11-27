package Sockets;

public class Fetcher {
	
	
	public static void main(String[] args) {		
		
		String APIKEY = "198f79089ba38013804c115cdebcc857";		
		String artist = "Madonna";
		String track = "Holiday";
		String page = HttpFetcher.download("ws.audioscrobbler.com", "/2.0?artist=" + artist + 
						"&track=" + track + 
						"&api_key=" + APIKEY + 
						"&method=track.getInfo&format=json");
		
		System.out.println(page);
		int start = page.indexOf("\n\n");
		page = page.substring(start);
		System.out.println(page);
				
		
		
	}

}
