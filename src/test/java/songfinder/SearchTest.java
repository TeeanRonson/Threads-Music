package songfinder;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class SearchTest {

	@Test
	public void testValidQueriesSimple() {

		String testName = "testValidQueriesSimple";
		String searchFileName = "simpleSearchResults.json";
		String inputDir = "lastfm_simple";
		String outputFileName = "songsByArtistSimple.txt";
		String queriesFileName = "simpleQueries.json";
		
		Path actualOutput = Paths.get(ProjectTest.ACTUAL_DIR + "/" + searchFileName);
		Path expectedOutput = Paths.get(ProjectTest.EXPECTED_DIR + "/" + searchFileName);
		
		String[] args = {"-input", ProjectTest.INPUT_DIR + "/" + inputDir,
				"-output", ProjectTest.ACTUAL_DIR + "/" + outputFileName,
				"-order", "artist",
				"-threads", "5",
				"-searchInput", ProjectTest.QUERIES_DIR + "/" + queriesFileName,
				"-searchOutput", actualOutput.toString()};
		
		ProjectTest.checkProjectJsonOutput(testName, 
				args,
				actualOutput,
				expectedOutput);		
	}

	@Test
	public void testValidQueriesComplex() {

		String testName = "testValidQueriesComplex";
		String searchFileName = "searchResults.json";
		String inputDir = "lastfm_subset";
		String outputFileName = "songsByArtistSubset.txt";
		String queriesFileName = "queries.json";
		
		Path actualOutput = Paths.get(ProjectTest.ACTUAL_DIR + "/" + searchFileName);
		Path expectedOutput = Paths.get(ProjectTest.EXPECTED_DIR + "/" + searchFileName);
		
		String[] args = {"-input", ProjectTest.INPUT_DIR + "/" + inputDir,
				"-output", ProjectTest.ACTUAL_DIR + "/" + outputFileName,
				"-order", "artist",
				"-threads", "5",
				"-searchInput", ProjectTest.QUERIES_DIR + "/" + queriesFileName,
				"-searchOutput", actualOutput.toString()};
		
		ProjectTest.checkProjectJsonOutput(testName, 
				args,
				actualOutput,
				expectedOutput);		
	}

	
	@Test
	public void testInvalidSearchInputPath() {
		
		String testName = "testInvalidSearchInputPath";
		String searchFileName = "searchResults.json";
		String inputDir = "lastfm_simple";
		String outputFileName = "songsByArtistSimple.txt";
		String queriesFileName = "BADPATH.json";

				
		Path actualOutput = Paths.get(ProjectTest.ACTUAL_DIR + "/" + searchFileName);		
	
		String[] args = {"-input", ProjectTest.INPUT_DIR + "/" + inputDir,
				"-output", ProjectTest.ACTUAL_DIR + "/" + outputFileName,
				"-order", "artist",
				"-threads", "5",
				"-searchInput", ProjectTest.QUERIES_DIR + "/" + queriesFileName,
				"-searchOutput", actualOutput.toString()};
		ProjectTest.checkExceptions(testName, args); 
	}

	@Test
	public void testEmptySearchInput() {
		String testName = "testEmptySearchInput";
		String searchFileName = "searchResults.json";
		String inputDir = "lastfm_simple";
		String outputFileName = "songsByArtistSimple.txt";
		String queriesFileName = "empty.json";
		
		
		Path actualOutput = Paths.get(ProjectTest.ACTUAL_DIR + "/" + searchFileName);
	
		String[] args = {"-input", ProjectTest.INPUT_DIR + "/" + inputDir,
				"-output", ProjectTest.ACTUAL_DIR + "/" + outputFileName,
				"-order", "artist",
				"-threads", "5",
				"-searchInput", ProjectTest.QUERIES_DIR + "/" + queriesFileName,
				"-searchOutput", actualOutput.toString()};
		ProjectTest.checkExceptions(testName, args); 
	}

	@Test
	public void testMissingQueryType() {
		String testName = "testMissingQueryType";
		String searchFileName = "searchResults.json";
		String inputDir = "lastfm_simple";
		String outputFileName = "songsByArtistSimple.txt";
		String queriesFileName = "noTitle.json";
		
		
		Path actualOutput = Paths.get(ProjectTest.ACTUAL_DIR + "/" + searchFileName);
		Path expectedOutput = Paths.get(ProjectTest.EXPECTED_DIR + "/" + "searchMissingType.json");
		
		String[] args = {"-input", ProjectTest.INPUT_DIR + "/" + inputDir,
				"-output", ProjectTest.ACTUAL_DIR + "/" + outputFileName,
				"-order", "artist",
				"-threads", "5",
				"-searchInput", ProjectTest.QUERIES_DIR + "/" + queriesFileName,
				"-searchOutput", actualOutput.toString()};
		ProjectTest.checkProjectJsonOutput(testName, 
				args,
				actualOutput,
				expectedOutput);		
	}
	
}