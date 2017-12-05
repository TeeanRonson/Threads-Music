package Utilities;

import java.util.Comparator;
import BaseObjects.SingleArtistInfo;

public class ByPlayCountSorter implements Comparator<SingleArtistInfo> {

	@Override
	public int compare(SingleArtistInfo o1, SingleArtistInfo o2) {
		
		int result0 = o1.getPlayCount();
		int result1 = o2.getPlayCount();
		
		return result1 - result0;
	}
	
	

}
