package songfinder;

public class SingleSongInfo {
	private String artist; 
	private String title; 
	private String trackId;
	
	public SingleSongInfo(String artist, String title, String trackId) {
		this.artist = artist; 
		this.title = title;
		this.trackId = trackId;
		
	}
	
	public String getArtist() {
		return this.artist;
	}
	
	public String getTitle() {
		return this.title;
	}

	public String getTrackId() {
		return this.trackId;
	}
	
	public String toString() {
		return null;
	}
	
	



}



