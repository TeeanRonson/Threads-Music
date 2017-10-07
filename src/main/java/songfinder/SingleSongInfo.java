package songfinder;

public class SingleSongInfo {
	private String artist; 
	private String title; 
	private String trackId;
	private String tags;
	 
	public SingleSongInfo(String artist, String title, String trackId, String tags) {
		this.artist = artist; 
		this.title = title;
		this.trackId = trackId;
		this.tags = tags;
		
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
	
	public String getTag() {
		return this.tags;
	}
	
	public String toString() {
		return null;
	}
	
	



}



