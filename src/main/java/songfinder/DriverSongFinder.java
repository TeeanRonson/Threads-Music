
package songfinder;

import java.util.HashMap;

/**
 * Main class for SongFinder lab and projects.
 * @author srollins
 *
 */
public class DriverSongFinder {
	/*
	 * Main method.
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (args.length != 6 && args.length != 8) {
			System.out.println("Incorrect arguments length");
			
		} else { 
			
			HashMap<String, String> checker = new HashMap<String, String>();
			String input = "-input";
			String output = "-output";
			String order = "-order";
			String threads = "-threads";
			MyLibrary ml = new MyLibrary();
			LibraryBuilder lb = new LibraryBuilder();
		
			for (int i = 0; i < args.length; i += 2) {
				checker.put(args[i], args[i + 1]);
			}
	
			if (!checker.containsKey(input) || !checker.containsKey(output) || !checker.containsKey(order)) {
				System.out.println("Incorrect arguments");
			
			} else {
			
				try {
				
					if (!checker.containsKey(threads) || checker.get(threads).isEmpty() || Integer.valueOf(checker.get(threads)) < 1 || Integer.valueOf(checker.get(threads)) > 1000) {
						checker.put(threads, "10");
					}
				
					ml = lb.buildLibrary(checker.get(input), Integer.valueOf(checker.get(threads)));
					ml.artistAndTitleOutput(checker.get(output), checker.get(order));
				
				} catch (NumberFormatException e) {
					e.getLocalizedMessage();
					System.out.println("Argument value is not an integer");
				}
			}		
		}
	}	
}
