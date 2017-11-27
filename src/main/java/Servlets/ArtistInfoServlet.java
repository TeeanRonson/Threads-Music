package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import songfinder.MyLibrary;
import songfinder.SingleSongInfo;

public class ArtistInfoServlet extends BaseServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		MyLibrary library = (MyLibrary) getServletConfig().getServletContext().getAttribute(LIBRARY);
		
		PrintWriter out = response.getWriter();
	
		out.println(header("SongFinder"));
		out.println("<h1>Song Finder</h1>");
		out.println("<p>Welcome to SongFinder! Search for an artist, song title or tag and we will give you a list of similar songs you might like. </p>");
		out.println("<hr/>");
		out.println("<p><form method=\"post\" action=\"artistinfo\"");
		out.println("<label>Search type:</label>");
		out.println("<select options:\"choices\" name=\"type\">");
		out.println("<option value =\"artist\">Artist</option><option value=\"title\">Title</option><option value =\"tag\">Tag</option>");
		out.println("</select>");
		out.println("<label>Query: </label>");
		out.println("<input type=\"text\" class=\"tftextinput\" name=\"q\" size=\"21\" maxlength=\"60\">");
		out.println("<input type=\"submit\" value=\"submit\" class=\"tfbutton\">");
		out.println("<p><a href=\"/home\">Home</a> <a href=\"/artist\">All Artists</a></p>");
		out.println("</form><div class=\"tfclear\"></div></div></p>");
		
		String artist = request.getParameter("artist");
		
		out.println("<p>Artist: " + artist + "</p>");
		out.println("<p>Listeners: </p>");
		out.println("<p>Play count: </p>");
		out.println("<p>Biography: </p>");
		out.println("<table style=\"width:50%\" border=\"1\">");
		out.println("<thead><th>Song Title</th></tr></thead>");
		
		TreeSet<SingleSongInfo> songs = library.getByArtist().get(artist);
	
		for (SingleSongInfo x: songs) {
			out.println("<tr>");
			out.println("<td>" + x.getTitle() + "</td>");
			out.println("</tr>");
		}
		out.println("</table>");
		out.println(footer());
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		MyLibrary library = (MyLibrary) getServletConfig().getServletContext().getAttribute(LIBRARY);
		
		PrintWriter out = response.getWriter();
	
		out.println(header("SongFinder"));
		out.println("<h1>Song Finder</h1>");
		out.println("<p>Welcome to SongFinder! Search for an artist, song title or tag and we will give you a list of similar songs you might like. </p>");
		out.println("<hr/>");
		out.println("<p><form method=\"post\" action=\"home\"");
		out.println("<label>Search type:</label>");
		out.println("<select options:\"choices\" name=\"type\">");
		out.println("<option value =\"artist\">Artist</option><option value=\"title\">Title</option><option value =\"tag\">Tag</option>");
		out.println("</select>");
		out.println("<label>Query: </label>");
		out.println("<input type=\"text\" class=\"tftextinput\" name=\"q\" size=\"21\" maxlength=\"60\">");
		out.println("<input type=\"submit\" value=\"submit\" class=\"tfbutton\">");
		out.println("<p><a href=\"/home\">Home</a><a href=\"/artist\">All Artists</a></p>");
		out.println("</form><div class=\"tfclear\"></div></div></p>");
		out.println("<table style=\"width:50%\" border=\"1\">");
		out.println("<thead><tr><th>Artist</th><th>Song Title</th></tr></thead>");
		
		JsonArray similars = new JsonArray();
		JsonObject object = new JsonObject();
		String type = request.getParameter("type");
		String query = request.getParameter("query");
		
		
		if (type.equals("artist")) {
			object = library.searchByArtist(query);
		}	

		if (type.equals("title")) {
			object = library.searchByTitle(query);
		}
		
		if (type.equals("tag")) {
			object = library.searchByTag(query);
		}
		
		if (object.get("similars") != null) {
			similars = object.get("similars").getAsJsonArray();
		}	
		
		
		for (JsonElement x: similars) {
			String artistName = ((JsonObject)x).get("artist").getAsString();
			String songTitle = ((JsonObject)x).get("title").getAsString();
			String id = ((JsonObject)x).get("trackId").getAsString();
			
			out.println("<tr>");
			out.println("<td>" + "<a href=\"/artistinfo?artist=" + artistName + "\">" + artistName + "</a></td>");
			out.println("<td>" + "<a href=\"/songinfo?trackId=" + id + "\">" + songTitle + "</a></td>");
			out.println("</tr>");
		}
		out.println("</table>");
		out.println(footer());
		
		
	}
}
