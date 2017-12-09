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
import Libraries.MyArtistLibrary;
import Libraries.MyMusicLibrary;
import Utilities.RejectedExecutionException;

public class LibraryBuilder {
	/**
	 * Declares a private data member
	 */
	private MyMusicLibrary addToLibrary;
	private MyArtistLibrary addToArtiLibrary; 
	/**
	 * Constructor takes no input and initialises
	 * the private data member
	 */
	public LibraryBuilder() {
		this.addToLibrary = new MyMusicLibrary();
		this.addToArtiLibrary = new MyArtistLibrary();
	}
	
	/**
	 * Public method takes as input the input directory. 
	 * Converts the directory into a Path object and 
	 * walks the file path to extract all files in 
	 * all sub-folders
	 * @param directory
	 * @return
	 * @throws RejectedExecutionException 
	 * @throws InterruptedException 
	 */
	public MyMusicLibrary buildMusicLibrary(String directory, int threadsSize) {
		
		Path path = Paths.get(directory);
		WorkQueue wq = new WorkQueue(threadsSize); 
		
		try (Stream<Path> paths = Files.walk(path)) {
			
			paths.forEach(p -> {
				try {
					wq.execute(new SongWorker(this.addToLibrary, p));
					
				} catch (RejectedExecutionException e) {
					System.out.println("Queue has been closed");
				}
			});
		
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		wq.shutDown();
		wq.awaitTermination();
		return this.addToLibrary;
	}
	
	/**
	 * Public method takes as input the input directory. 
	 * Converts the directory into a Path object and 
	 * walks the file path to extract all files in 
	 * all sub-folders
	 * @param directory
	 * @param threadsSize
	 * @return
	 */
	public MyArtistLibrary buildArtistLibrary(String directory, int threadsSize) {
		
		Path path = Paths.get(directory);
		WorkQueue wq = new WorkQueue(threadsSize); 
		
		try (Stream<Path> paths = Files.walk(path)) {
			
			paths.forEach(p -> {
				try {
					wq.execute(new ArtistWorker(this.addToArtiLibrary, p));
					
				} catch (RejectedExecutionException e) {
					System.out.println("Queue has been closed");
				}
			});
		
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		wq.shutDown();
		wq.awaitTermination();
		return this.addToArtiLibrary;
	}
	
}