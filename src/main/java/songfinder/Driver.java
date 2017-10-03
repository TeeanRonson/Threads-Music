package songfinder;

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
		
//		System.out.println(args[0]);
		
		if (args.length == 6) {
			
//			System.out.println(args[0]);
			
			String input = args[1];
			String output = args[3];
			
			LibraryBuilder lb = new LibraryBuilder();
			MyLibrary ml = new MyLibrary();
			
			ml = lb.buildLibrary(input);
			
			lb.buildLibrary(output);
			
		} else { 
			System.out.println("Unable to process arguments");
			System.exit(0);
		}
		
	}

}
