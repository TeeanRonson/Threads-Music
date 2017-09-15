package songfinder;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class LibraryTest {

	protected static final String INPUTFLAG = "-input";
	protected static final String OUTPUTFLAG = "-output";
	protected static final String ORDERFLAG = "-order";
	
	protected static final String ARTIST = "artist";
	protected static final String TITLE = "title";
	protected static final String TAG = "tag";
		
	@Test
	public void testBadInputPath() {
		ProjectTest.checkExceptions("testBadInputPath", new String[] {INPUTFLAG, "/BAD/PATH", OUTPUTFLAG, (ProjectTest.ACTUAL_DIR + "/badInputPath.txt"), ORDERFLAG, ARTIST});		
	}

	@Test
	public void testBadOutputPath() {
		ProjectTest.checkExceptions("testBadOutputPath", new String[] {INPUTFLAG, (ProjectTest.INPUT_DIR + "/lastfm_simple"), OUTPUTFLAG, "/BAD/PATH", ORDERFLAG, ARTIST});		
	}

	@Test
	public void testBadOrderValue() {
		ProjectTest.checkExceptions("testBadOrderValue", new String[] {INPUTFLAG, (ProjectTest.INPUT_DIR + "/lastfm_simple"), OUTPUTFLAG, (ProjectTest.ACTUAL_DIR + "/badOrderValue.txt"), ORDERFLAG, "BADORDER"});		
	}
	
	@Test
	public void testSimpleByArtist() {
		String file = "songsByArtistSimple.txt";
		String inputLocation = "lastfm_simple";
		String test = "testSimpleByArtist";
		runTest(file, inputLocation, test, ARTIST);		
	}
	
	@Test
	public void testSimpleByTitle() {
		String file = "songsByTitleSimple.txt";
		String inputLocation = "lastfm_simple";
		String test = "testSimpleByTitle";
		runTest(file, inputLocation, test, TITLE);		
	}

	@Test
	public void testSimpleByTag() {
		String file = "songsByTagSimple.txt";
		String inputLocation = "lastfm_simple";
		String test = "testSimpleByTag";
		runTest(file, inputLocation, test, TAG);		
		
	}
	
	@Test
	public void testSimpleByArtistWithTxtFile() {
		String file = "songsByArtistWithTxtFile.txt";
		String inputLocation = "lastfm_txtfile";
		String test = "testSimpleByArtistWithTxtFile";
		runTest(file, inputLocation, test, ARTIST);		
		
	}
		
	@Test
	public void testByArtist() {
		String file = "songsByArtistSubset.txt";
		String inputLocation = "lastfm_subset";
		String test = "testByArtist";
		runTest(file, inputLocation, test, ARTIST);		
		
	}

	@Test
	public void testByTitle() {
		String file = "songsByTitleSubset.txt";
		String inputLocation = "lastfm_subset";
		String test = "testByTitle";
		runTest(file, inputLocation, test, TITLE);		
		
	}
	
	@Test
	public void testByTag() {
		String file = "songsByTagSubset.txt";
		String inputLocation = "lastfm_subset";
		String test = "testByTag";
		runTest(file, inputLocation, test, TAG);		
		
	}

	
	protected void runTest(String file, String inputLocation, String test, String order) {
		Path actual = Paths.get(ProjectTest.ACTUAL_DIR + "/" + file);
		Path expected = Paths.get(ProjectTest.EXPECTED_DIR + "/" + file);
		
		String[] args = new String[] {INPUTFLAG, (ProjectTest.INPUT_DIR + "/" + inputLocation), OUTPUTFLAG, (ProjectTest.ACTUAL_DIR + "/" + file), ORDERFLAG, order}; 
		ProjectTest.checkProjectOutput(test, 
				args, 
				actual, 
				expected);		
	}
	
}
