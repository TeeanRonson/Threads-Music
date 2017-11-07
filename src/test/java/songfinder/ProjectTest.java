package songfinder;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Original author sjengle, modified by srollins.
 *
 */
public class ProjectTest {

	// Project Configuration

	public static final String INPUT_DIR  = "input";
	public static final String EXPECTED_DIR = "expected";
	public static final String ACTUAL_DIR = "actual";
	public static final String QUERIES_DIR = "queries";

	public static final String[] DEFAULT_ARGS = {"-input", "input/lastfm_subset",
			"-output", "actual/songsByArtistSubset.txt",
			"-order", "artist"};


	/**
	 * Checks whether environment setup is correct, with a input and output
	 * directory located within the base directory.
	 */
	public static boolean isEnvironmentSetup() {
		Path input  = Paths.get(INPUT_DIR);
		Path output = Paths.get(EXPECTED_DIR);
		Path result = Paths.get(ACTUAL_DIR);

		try {
			if(!Files.exists(result)) {
				Files.createDirectories(result);	
			}        	
		}
		catch (Exception e) {
			return false;
		}

		return Files.isReadable(input)  && Files.isReadable(output) && Files.isWritable(result);
	}

	/**
	 * Checks line-by-line if two files are equal. If one file contains extra
	 * blank lines at the end of the file, the two are still considered equal.
	 *
	 * @param path1 - path to first file to compare with
	 * @param path2 - path to second file to compare with
	 * @return positive value if two files are equal, negative value if not
	 *
	 * @throws IOException
	 */
	public static int checkFiles(Path path1, Path path2) throws IOException {
		Charset charset = java.nio.charset.StandardCharsets.UTF_8;

		// used to output line mismatch
		int count = 0;

		try (
				BufferedReader reader1 =
				Files.newBufferedReader(path1, charset);
				BufferedReader reader2 =
						Files.newBufferedReader(path2, charset);
				) {
			String line1 = reader1.readLine();
			String line2 = reader2.readLine();

			while (true) {
				count++;

				// compare lines until we hit a null (i.e. end of file)
				if ((line1 != null) && (line2 != null)) {
					// use consistent path separators
					line1 = line1.replaceAll(Matcher.quoteReplacement(File.separator), "/");
					line2 = line2.replaceAll(Matcher.quoteReplacement(File.separator), "/");

					// remove trailing spaces
					line1 = line1.trim();
					line2 = line2.trim();

					// check if lines are equal
					if (!line1.equals(line2)) {
						return -count;
					}

					// read next lines if we get this far
					line1 = reader1.readLine();
					line2 = reader2.readLine();
				}
				else {
					// discard extra blank lines at end of reader1
					while ((line1 != null) && line1.trim().isEmpty()) {
						line1 = reader1.readLine();
					}

					// discard extra blank lines at end of reader2
					while ((line2 != null) && line2.trim().isEmpty()) {
						line2 = reader2.readLine();
					}

					if (line1 == line2) {
						// only true if both are null, otherwise one file had
						// extra non-empty lines
						return count;
					}
					else {
						// extra blank lines found in one file
						return -count;
					}
				}
			}
		}
	}

	/**
	 * Checks whether {@link DriverSongFinder} will run without generating any exceptions.
	 * Will print the stack trace if an exception occurs. Designed to be used
	 * within an unit test.
	 *
	 * @param testName - name of test for debugging
	 * @param args - arguments to pass to {@link DriverSongFinder}
	 */
	public static void checkExceptions(String testName, String[] args) {
		try {
			DriverSongFinder.main(args);
		}
		catch (Exception e) {
			StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));

			Assert.fail(String.format(
					"%n" + "Test Case: %s%n" + "Exception: %s%n",
					testName, writer.toString()));
		}
	}

	/**
	 * Checks whether {@link DriverSongFinder} generates the expected output without any
	 * exceptions. Will print the stack trace if an exception occurs. Designed
	 * to be used within an unit test.
	 *
	 * @param testName - name of test for debugging
	 * @param args - arguments to pass to {@link DriverSongFinder}
	 * @param actual - name of actual file in result directory
	 * @param expected - name of expected file in output directory
	 */
	public static void checkProjectOutput(String testName, String[] args,
			Path actual, Path expected) {

		try {
			// Remove actual result file if it already exists
			Files.deleteIfExists(actual);

			// Check if parent directories need to be created
			if (Files.notExists(actual.getParent())) {
				Files.createDirectories(actual.getParent());
			}

			DriverSongFinder.main(args);

			int count = checkFiles(actual, expected);

			if (count <= 0) {
				Assert.fail(String.format("%n" + "Test Case: %s%n" +
						" Mismatched Line: %d%n", testName, -count));
			}
		}
		catch (Exception e) {
			StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));

			Assert.fail(String.format(
					"%n" + "Test Case: %s%n" + "Exception: %s%n",
					testName, writer.toString()));
		}
	}


	/**
	 * Checks whether {@link DriverSongFinder} generates the expected output without any
	 * exceptions. Will print the stack trace if an exception occurs. Designed
	 * to be used within an unit test.
	 *
	 * @param testName - name of test for debugging
	 * @param args - arguments to pass to {@link DriverSongFinder}
	 * @param actual - name of actual file in result directory
	 * @param expected - name of expected file in output directory
	 */
	public static void checkProjectJsonOutput(String testName, String[] args,
			Path actual, Path expected) {

		try {
			// Remove actual result file if it already exists
			Files.deleteIfExists(actual);

			// Check if parent directories need to be created
			if (Files.notExists(actual.getParent())) {
				Files.createDirectories(actual.getParent());
			}

			DriverSongFinder.main(args);

			JsonParser parser = new JsonParser();

			JsonObject actualObj = (JsonObject) parser.parse(Files.newBufferedReader(actual));
			JsonObject expectedObj = (JsonObject) parser.parse(Files.newBufferedReader(expected));

			if(!actualObj.equals(expectedObj)) {
				Assert.fail(String.format("%n" + "Test Case: %s%n" +
						" JSON result does not match.%n", testName));
			} 
		}
		catch (Exception e) {
			StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));

			Assert.fail(String.format(
					"%n" + "Test Case: %s%n" + "Exception: %s%n",
					testName, writer.toString()));
		}
	}

	/**
	 * Tests whether environment setup is correct.
	 */
	@Test
	public void testEnvironment() {
		String errorMessage = String.format(
				"%n" + "Test Case: %s%n" + "%s%n",
				"Environment Setup",
				"Check that you have a readable input, result, and output " +
				"directories in your base directory.");

		Assert.assertTrue(errorMessage, isEnvironmentSetup());
	}

	/**
	 * This test checks if Driver runs without exceptions when given no
	 * arguments.
	 */
	@Test
	public void testNoArguments() {
		checkExceptions("No Arguments", new String[] {});
	}

	/**
	 * This test checks if Driver runs without exceptions when given bad
	 * arguments.
	 */
	@Test
	public void testBadArguments() {
		checkExceptions("Bad Arguments", new String[] {"hello", "world"});
	}
}
