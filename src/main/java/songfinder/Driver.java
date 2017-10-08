
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
		//TODO: Design program and complete main method.
		
		if (args.length == 6) {
			
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
				ml.artistAndTitleOutput("/Users/Rong/git/lab-3-TeeanRonson/testOutput/output1", checker.get(order));
				System.out.println("Driver result: " + ml.checkSize());
			}

		} else { 
			System.out.println("Unable to processs arguments");
			System.exit(1);
		}	
	}

}
