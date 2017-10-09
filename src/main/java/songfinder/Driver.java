
package songfinder;

import java.util.HashMap;

/**
 * Main class for SongFinder lab and projects.
 * @author srollins
 *
 */
public class Driver {

	/*
	 * Main method.
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (args.length != 6 && args.length == 0) {
			System.out.println("Unable to processs arguments");
		}	
			
		HashMap<String, String> checker = new HashMap<String, String>();
		String input = "-input";
		String output = "-output";
		String order = "-order";
		MyLibrary ml = new MyLibrary();
		LibraryBuilder lb = new LibraryBuilder();
			
		for (int i = 0; i < args.length; i += 2) {
			checker.put(args[i], args[i + 1]);
		}
			
		if (checker.containsKey(input) && checker.containsKey(output) && checker.containsKey(order)) {		
			ml = lb.buildLibrary(checker.get(input));
			ml.artistAndTitleOutput(checker.get(output), checker.get(order));
		} else { 
			System.out.println("Incorrect arguments");
		}		
	}
}
