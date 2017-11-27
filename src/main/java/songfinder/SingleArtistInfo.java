package songfinder;

public class SingleArtistInfo {

	private String artist; 
	private int noOfListeners; 
	private int playCount; 
	private String biography; 
	
	public SingleArtistInfo(String artistName, int listeners, int count, String bio) {
		this.artist = artistName; 
		this.noOfListeners = listeners; 
		this.playCount = count; 
		this.biography = bio;
	}
	
	
	public String getArtist() {
		return this.artist;
	}
	
	public int getListeners() {
		return this.noOfListeners;
	}
	
	public int getPlayCount() {
		return this.playCount;
	}
	
	public String getBio() {
		return this.biography;
	}
}
