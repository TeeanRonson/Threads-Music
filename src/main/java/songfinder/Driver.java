
package songfinder;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
// class in a file thats not named properly
import java.util.HashMap;
import java.util.TreeSet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Utilities.RejectedExecutionException;

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
	public static void main(String[] args) throws RejectedExecutionException {
		
		MyLibrary ml = new MyLibrary();
		LibraryBuilder lb = new LibraryBuilder();
		
		if (args.length != 6 && args.length != 8 && args.length != 12) {
			System.out.println("Incorrect arguments length");
			
		} else { 
			
			HashMap<String, String> checker = new HashMap<String, String>();
			String input = "-input";
			String output = "-output";
			String order = "-order";
			String threads = "-threads";
			String searchInput = "-searchInput";
			String searchOutput = "-searchOutput";
			
			for (int i = 0; i < args.length - 1; i += 2) {
				checker.put(args[i], args[i + 1]);
			}
	
			if (!checker.containsKey(input) || !checker.containsKey(output) || !checker.containsKey(order)) {
				System.out.println("Incorrect arguments");
			
			} else {
			
				try {
				
					if (!checker.containsKey(threads) || checker.get(threads).isEmpty() || Integer.valueOf(checker.get(threads)) < 1 || Integer.valueOf(checker.get(threads)) > 1000) {
						checker.put(threads, "10");
					}
				
				} catch (NumberFormatException e) {
					System.out.println("Argument value is not an integer");
					checker.put(threads, "10");
				}
				
				ml = lb.buildLibrary(checker.get(input), Integer.valueOf(checker.get(threads)));
				ml.artistAndTitleOutput(checker.get(output), checker.get(order));
				ml.searchResultsOutput(checker.get(searchInput), checker.get(searchOutput));
			}		
		}
	}	
}
