package songfinder;
import java.nio.file.FileSystems;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	ThreadTest.ThreadConfigurationTest.class,
	ThreadTest.ThreadOutputTest.class	
})
public class ThreadTest {

	/** Configure this on your system if you want to have a longer timeout. */
	public static final int TIMEOUT = 60000;


	/* Arguments
	 * Required:
	 -input <input_path> 
	 -output <output_path> 
	 -order <tag|artist|title>

	 * Optional:
	-threads <number_threads> 
	-queries <query_path>
	 */			

	/* Arguments
	 * Required:
	 -input <input_path> 
	 -output <output_path> 
	 -order <tag|artist|title>

	 * Optional:
	-threads <number_threads> 
	-queries <query_path>
	 */			

	public static class ThreadOutputTest {

		@Test(timeout = TIMEOUT)
		public void testSimpleTenThreads() {
			runTest("testSimpleTenThreads", "lastfm_simple", "songsByArtistSimple.txt", "artist", "10");			
		}
		
		@Test(timeout = TIMEOUT)
		public void testComplexTenThreads() {
			runTest("testComplexTenThreads", "lastfm_subset", "songsByArtistSubset.txt", "artist", "10");			

		}

		@Test(timeout = TIMEOUT)
		public void testComplexOneThread() {
			runTest("testComplexOneThread", "lastfm_subset", "songsByArtistSubset.txt", "artist", "1");			
		}

		@Test(timeout = TIMEOUT)
		public void testComplexFiveThreads() {
			runTest("testComplexFiveThreads", "lastfm_subset", "songsByArtistSubset.txt", "artist", "5");			

		}

	}
	
	public static class ThreadConfigurationTest {

		@Test(timeout = TIMEOUT)
		public void testNumberThreadsDecimal() {			
			
			String testName = "testNumberThreadsDecimal";
			String[] args = {"-input", "input/lastfm_simple",
					"-output", "results/songsByArtistSimple.txt",
					"-order", "artist",
					"-threads", "5.2"};

			ProjectTest.checkExceptions(testName, args); 
		}

		@Test(timeout = TIMEOUT)
		public void testNumberThreadsNegative() {

			String testName = "testNumberThreadsNegative";
			String[] args = {"-input", "input/lastfm_simple",
					"-output", "results/songsByArtistSimple.txt",
					"-order", "artist",
					"-threads", "-4"};

			ProjectTest.checkExceptions(testName, args); 
		}

		@Test(timeout = TIMEOUT)
		public void testNumberThreadsString() {

			String testName = "testNumberThreadsString";
			String[] args = {"-input", "input/lastfm_simple",
					"-output", "results/songsByArtistSimple.txt",
					"-order", "artist",
					"-threads", "A1"};

			ProjectTest.checkExceptions(testName, args); 
		}
	}

	
	protected static void runTest(String testName, String inputDir, String outputFile, String order, String threads) {
		
		String[] args = {"-input", ProjectTest.INPUT_DIR + "/" + inputDir,
				"-output", ProjectTest.ACTUAL_DIR + "/" + outputFile,
				"-order", order,
				"-threads", threads};

		ProjectTest.checkProjectOutput(testName, args, 
				FileSystems.getDefault().getPath(ProjectTest.ACTUAL_DIR + "/" + outputFile), //actual 
				FileSystems.getDefault().getPath(ProjectTest.EXPECTED_DIR + "/" + outputFile)); //expected
		
	}
}