package songfinder;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.TreeSet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Worker implements Runnable {
	
	private MyLibrary library; 
	private Path p;
	
	public Worker(MyLibrary library, Path p) {
		this.library = library;
		this.p = p;
	}

	@Override
	public void run() {
		
		SingleSongInfo ssi = null;
		TreeSet<String> tagList = new TreeSet<String>();
	
		if (p.toString().toLowerCase().endsWith(".json")) {
			
			Gson gson = new Gson();
			
			try (FileReader fr = new FileReader(p.toFile().getAbsolutePath())) {
				
				JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(fr);
				JsonObject obj = element.getAsJsonObject();
			
				String artist = obj.get("artist").getAsString();
				String title = obj.get("title").getAsString();
				String trackId = obj.get("track_id").getAsString();
				JsonArray getTags = obj.getAsJsonArray("tags");
				String tags = "";
				
				for (int i = 0; i < getTags.size(); i++) {
					JsonArray internalArray = (JsonArray) getTags.get(i);
					tags = internalArray.get(0).getAsString();
					tagList.add(tags);
				}		
				
				ssi = new SingleSongInfo(artist, title, trackId, tagList);
			
				if (ssi != null) {
					this.library.addNewSong(ssi);
				}
				
			} catch (IOException e) {
				e.getMessage();
				System.out.println("Can't find file");
			}
		}
	}
}
