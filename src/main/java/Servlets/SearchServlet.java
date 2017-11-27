package Servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import songfinder.MyLibrary;

public class SearchServlet extends BaseServlet{
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		MyLibrary library = (MyLibrary) getServletConfig().getServletContext().getAttribute(LIBRARY);
		
		PrintWriter out = response.getWriter();
	
		out.println(header("SongFinder"));
		out.println("<h1>Song Finder</h1>");
		out.println("<p>Welcome to SongFinder! Search for an artist, song title or tag and we will give you a list of similar songs you might like. </p>");
		out.println("<hr/>");
		
		out.println("<p><form method=\"post\" action=\"search\"");
		out.println("<label>Search type:</label>");
		out.println("<select options:\"choices\" name=\"type\">");
		out.println("<option value =\"artist\">Artist</option><option value=\"title\">Title</option><option value =\"tag\">Tag</option>");
		out.println("</select>");
		
		out.println("<label>Query: </label>");
		out.println("<input type=\"text\" class=\"tftextinput\" name=\"q\" size=\"21\" maxlength=\"60\">");
		out.println("<input type=\"submit\" value=\"submit\" class=\"tfbutton\">");
		
		out.println("<p><a href=\"http://localhost:8080/artist\">All Artists</a></p>");
		
		out.println("</form><div class=\"tfclear\"></div></div></p>");
		
		out.println("<table style=\"width:50%\" border=\"1\">");
		out.println("<thead><tr><th>Artist</th><th>Song Title</th></tr></thead>");
		
		JsonArray similars = new JsonArray();
		JsonObject object = new JsonObject();
		String type = request.getParameter("type");
		String query = request.getParameter("query");
		String artist = "artist";
		String title = "title";
		String tag = "tag";
		
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
		
		for (JsonElement x: similars) {
			out.println("<tr>");
			out.println("<td>" + ((JsonObject)x).get(artist).getAsString() + "</td>");
			out.println("<td>" + ((JsonObject)x).get(title).getAsString() + "</td>");
			out.println("</tr>");
		}
		out.println("</table>");
		out.println(footer());
		
		
	}
}
