package songfinder;

import java.util.Comparator;

public class ByArtistSorter implements Comparator<SingleSongInfo> {

	@Override
	public int compare(SingleSongInfo o1, SingleSongInfo o2) {
		
		int result0 = o1.getArtist().compareTo(o2.getArtist());
		int result1 = o1.getTitle().compareTo(o2.getTitle());
		
		if (result0 == 0 && result1 == 0) {
			return o1.getTrackId().compareTo(o2.getTrackId());
		} 
		else if (result0 == 0) {
			return result1;
		} else {
			return result0;
		}
	}	
}
