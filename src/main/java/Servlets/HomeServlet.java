package Servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import songfinder.LibraryBuilder;
import songfinder.MyLibrary;

public class HomeServlet extends BaseServlet{

	BaseServlet bs = new BaseServlet();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		PrintWriter out = response.getWriter();
		
		out.println(bs.header("SongFinder"));
		out.println("<h1>Song Finder</h1>");
		out.println("<p>Welcome to SongFinder! Search for an artist, song title or tag and we will give you a list of similar songs you might like. </p>");
		out.println("<hr/>");
		out.println("<p><form id=\"newsearch\" method=\"post\" action=\"home\"");
		out.println(" <label>Search type:</label>");
		out.println("<select options: \"choices\" name=\"type\">");
		out.println("<option value = \"artist\">Artist</option> <option value = \"title\">Title</option> <option value = \"tag\">Tag</option>");
		out.println("</select>");
		out.println("<label>Query: </label>");
		out.println("<input type=\"text\" class=\"tftextinput\" name=\"query\" size=\"21\" maxlength=\"60\">");
		out.println("<input type=\"submit\" value=\"submit\" class=\"tfbutton\">");
		out.println("<p><a href=\"http://localhost:8080/artist\">Artists</a></p>");				// All Artists Link 
		out.println("</form><div class=\"tfclear\"></div></div></p>");
		out.println(bs.footer());
	
	}
			
		
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		MyLibrary library = (MyLibrary) getServletConfig().getServletContext().getAttribute(LIBRARY);
		
		
		
		PrintWriter out = response.getWriter();
		
	
		out.println(bs.header("SongFinder"));
		out.println("<h1>Song Finder</h1>");
		out.println("<p>Welcome to SongFinder! Search for an artist, song title or tag and we will give you a list of similar songs you might like. </p>");
		out.println("<hr/>");
		out.println("<p><form id=\"newsearch\" method=\"post\" action=\"home\"");
		out.println(" <label>Search type:</label>");
		out.println("<select options: \"choices\" name=\"type\">");
		out.println("<option value = \"artist\">Artist</option> <option value = \"title\">Title</option> <option value = \"tag\">Tag</option>");
		out.println("</select>");
		out.println("<label>Query: </label>");
		out.println("<input type=\"text\" class=\"tftextinput\" name=\"q\" size=\"21\" maxlength=\"60\">");
		out.println("<input type=\"submit\" value=\"submit\" class=\"tfbutton\">");
		out.println("<p><a href=\"http://localhost:8080/artist\">Artists</a></p>");	
		out.println("</form><div class=\"tfclear\"></div></div></p>");
		out.println("<table border=\"1\">");
		out.println("<thead><tr><th>Artist</th><th>Song Title</th></tr></thead>");
		out.println(bs.debugger("hello1")); // this occurs before the table headers. Why?
//		out.println(library.getByArtist().keySet()); // Checking the data structure through the context shows that it is not empty
		
		JsonArray similars = new JsonArray();
		JsonObject object = new JsonObject();
		String type = request.getParameter("type");
		String query = request.getParameter("query");
		String artist = "Artist";
		String title = "Title";
		String tag = "Tag";
		
		if (type.equals(artist)) {
			object = library.searchByArtist(query);
		}	

		if (type.equals(title)) {
			object = library.searchByTitle(query);
		}
		
		if (type.equals(tag)) {
			object = library.searchByTag(query);
		}
		
		if (object.get("similars") != null) {
			similars = object.get("similars").getAsJsonArray();
		}	
		
		// the for loop is not read 
		for (JsonElement x: similars) {
			out.println("<tr>");
			out.println("<td>" + ((JsonObject)x).get(artist).getAsString() + "</td>");
			out.println("<td>" + ((JsonObject)x).get(title).getAsString() + "</td>");
			out.println("</tr>");
		}
		out.println("</table>");
		out.println(bs.debugger("hello2"));
		
		out.println(bs.footer());
		
		
	}

}
