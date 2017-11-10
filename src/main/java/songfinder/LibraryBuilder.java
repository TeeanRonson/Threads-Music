package songfinder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Generics.WorkQueue;

public class LibraryBuilder {
	/**
	 * Declares a private data member
	 */
	private MyLibrary addToLibrary;
	/**
	 * Constructor takes no input and initialises
	 * the private data member
	 */
	public LibraryBuilder() {
		this.addToLibrary = new MyLibrary();
	}
	
	/**
	 * Public method takes as input the input directory. 
	 * Converts the directory into a Path object and 
	 * walks the file path to extract all files in 
	 * all sub-folders
	 * @param directory
	 * @return
	 * @throws InterruptedException 
	 */
	public MyLibrary buildLibrary(String directory, int threadsSize) {
		
		Path path = Paths.get(directory);
		WorkQueue wq = new WorkQueue(threadsSize); 
		
		try (Stream<Path> paths = Files.walk(path)) {
			
			paths.forEach(p -> wq.execute(new Worker(this.addToLibrary, p))); 
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		
		wq.shutDown();
		wq.awaitTermination();
		
		return this.addToLibrary;
	}
}