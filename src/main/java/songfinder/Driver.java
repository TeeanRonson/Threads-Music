
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
		
		if (args.length != 6 && args.length != 8) {
			System.out.println("Incorrect arguments length");
			
		} else { 
			
			HashMap<String, String> checker = new HashMap<String, String>();
			String input = "-input";
			String output = "-output";
			String order = "-order";
			String threads = "-threads";
			String searchInput = "-searchInput";
			String searchOutput = "-searchOutput";
			
			MyLibrary ml = new MyLibrary();
			LibraryBuilder lb = new LibraryBuilder();
		
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
				
				
			// -----------------------------------------------------------------------------------------------------------------------
				
				String inputPath = checker.get(searchInput).toLowerCase();
				TreeSet<String> artistNames = new TreeSet<String>();
				TreeSet<String> titleNames = new TreeSet<String>();
				TreeSet<String> tagNames = new TreeSet<String>();
				
				String artist = "";
				String title = "";
				String tag = "";		
				
				if (inputPath.endsWith(".json")) {
					
					try (FileReader fr = new FileReader(inputPath)) {
						
						JsonParser parser = new JsonParser();
						JsonElement element = parser.parse(fr);
						JsonObject obj = element.getAsJsonObject();
						
						JsonArray getArtist = obj.getAsJsonArray("searchByArtist");
						JsonArray getTitle = obj.getAsJsonArray("searchByArtist");
						JsonArray getTag = obj.getAsJsonArray("searchByArtist");
						
						for (int i = 0; i < getArtist.size(); i++) {
							artist = getArtist.get(i).getAsString();
							artistNames.add(artist);
						}
						
						for (int i = 0; i < getTitle.size(); i++) {
							title = getTitle.get(i).getAsString();
							titleNames.add(title);
						}
						
						for (int i = 0; i < getTag.size(); i++) {
							tag = getTag.get(i).getAsString();
							tagNames.add(tag);
						}
						
						
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				for (String x: artistNames) {
					ml.outputByArtist(x);
				}
				
				for (String y: titleNames) {
					ml.outputByTitle(y);
				}
				
				for (String z: tagNames) {
					ml.outputByTag(z);
				}
				
//				ml.outputByArtist("Casual");
//				ml.outputByTitle("Amor De Cabaret");
			}		
		}
	}	
}
