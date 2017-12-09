package Utilities;

import java.util.Comparator;

import BaseObjects.SingleSongInfo;

/**
 * Comparator to sort data structures. 
 * Pecking order: 
 * Title 
 * Artist 
 * TrackId
 * @author Rong
 *
 */
public class ByTitleSorter implements Comparator<SingleSongInfo> {
	
	@Override
	public int compare(SingleSongInfo o1, SingleSongInfo o2) {
			
		int result0 = o1.getTitle().compareTo(o2.getTitle());
		int result1 = o1.getArtist().compareTo(o2.getArtist());
			
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