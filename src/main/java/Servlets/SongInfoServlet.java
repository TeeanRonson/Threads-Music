package Servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import Libraries.MyMusicLibrary;
import Libraries.MyUserLibrary;

public class SongInfoServlet extends BaseServlet {
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		if (request.getSession().getAttribute(LOGGED_IN) == null || (boolean) request.getSession().getAttribute(LOGGED_IN) == false) {
			response.sendRedirect("/mainmenu");
		} 
		
		MyMusicLibrary mlibrary = (MyMusicLibrary) getServletConfig().getServletContext().getAttribute(LIBRARY);
		MyUserLibrary uLibrary = (MyUserLibrary) getServletConfig().getServletContext().getAttribute(USERLIBRARY);
		String username = (String) request.getSession().getAttribute(USERNAME);
		String lastLogin = uLibrary.getLastLogin(username);
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		
		out.println(body("Threads Music"));
		out.println("<center>");
		out.println("<img src=\"http://i64.tinypic.com/jkvnlz.jpg\" height= \"75\" width=\"100\" style=\"float:middle;\"/>");
		out.println("<p> Search for an artist, song title or popular genre! </p>");
		out.println("<p>" + username + "</p>");
		out.println("<p> Last login: " + lastLogin + "</p>");
		out.println("<hr/>");
		out.println("<p><form method=\"post\" action=\"home\"");
		out.println("<label>Search type:</label>");
		out.println("<select options: \"choices\" name=\"type\">");
		out.println("<option value = \"artist\">Artist</option> <option value = \"title\">Title</option> <option value = \"tag\">Tag</option>");
		out.println("</select>");
		out.println("<label>Query: </label>");
		out.println("<input type=\"text\" class=\"tftextinput\" name=\"query\" size=\"21\" maxlength=\"60\">");
		out.println("<input type=\"submit\" value=\"submit\" class=\"tfbutton\">");
		out.println("<p><a href=\"/home\">Home</a> <a href=\"/artist\">All Artists</a> <a href=\"history\"> History </a> <a href=\"mainmenu\">Log out</a></p>");			
		out.println("</form><div class=\"tfclear\"></div></div></p>");
		out.println("<table style=\"width:50%\" border=\"1\">");
		out.println("<thead><tr><th>Artist</th><th>Song Title</th></tr></thead>");
		
		String trackId = request.getParameter("trackId");
		
		String artist = mlibrary.getName(trackId);
		String title = mlibrary.getTitle(trackId);
		
		JsonObject object = new JsonObject(); // is there a difference 
		JsonArray similars = new JsonArray();
		
		object = mlibrary.searchHelper(title, "title");
		
		if (object.get("similars") != null) {
			similars = object.get("similars").getAsJsonArray();
		}	
		
		out.println("<p> Artist: " + artist + "</p>");
		out.println("<p> Title: " + title + "</p>" );
		out.println("<h4> Similar Songs </h4>");
		
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
		out.println(end());
		
				
	}
	
//	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		
//		if (request.getSession().getAttribute(LOGGED_IN) == null || (boolean) request.getSession().getAttribute(LOGGED_IN) == false) {
//			response.sendRedirect("/mainmenu");
//		} 
//		
//		response.setContentType("text/html");
//		response.setStatus(HttpServletResponse.SC_OK);
//		PrintWriter out = response.getWriter();
//	
//		out.println(body("Threads Music"));
//		out.println("<img src=\"http://i64.tinypic.com/jkvnlz.jpg\" height= \"75\" width=\"100\" style=\"float:left;\"/><h1>Threads Music</h1>");
//		out.println("<p>Search for an artist, song title or tag and we will give you a list of similar songs you might like. </p>");
//		out.println("<hr/>");
//		out.println("<p><form method=\"post\" action=\"home\"");
//		out.println("<label>Search type:</label>");
//		out.println("<select options:\"choices\" name=\"type\">");
//		out.println("<option value =\"artist\">Artist</option><option value=\"title\">Title</option><option value =\"tag\">Tag</option>");
//		out.println("</select>");
//		out.println("<label>Query: </label>");
//		out.println("<input type=\"text\" class=\"tftextinput\" name=\"query\" size=\"21\" maxlength=\"60\">");
//		out.println("<input type=\"submit\" value=\"submit\" class=\"tfbutton\">");
//		out.println("<p><a href=\"/home\">Home</a><a href=\"/artist\">All Artists</a> <a href=\"mainmenu\">Log out</a></p>");
//		out.println("</form><div class=\"tfclear\"></div></div></p>");
//		out.println("<table style=\"width:50%\" border=\"1\">");
//		out.println("<thead><tr><th>Artist</th><th>Song Title</th></tr></thead>");
//		
//		JsonArray similars = new JsonArray();
//		JsonObject object = new JsonObject();
//		String type = request.getParameter("type");
//		String query = request.getParameter("query");
//		System.out.println("This is the type:" + type);
//		System.out.println("this is the query:" + query);
//		
//		
//		if (query.equals(null)) {
//			response.sendRedirect("/home");
//		}
//		
//		if (type.equals("artist")) {
//			object = mlibrary.caseCheckArtist(query, "artist");
//		}	
//
//		if (type.equals("title")) {
//			object = mlibrary.caseCheckTitle(query, "title");
//		}
//		
//		if (type.equals("tag")) {
//			object = mlibrary.caseCheckTag(query);
//		}
//		
//		if (object.get("similars") != null) {
//			similars = object.get("similars").getAsJsonArray();
//		}	
//		
//		for (JsonElement x: similars) {
//			String artist = ((JsonObject)x).get("artist").getAsString();
//			String title = ((JsonObject)x).get("title").getAsString();
//			String trackId = ((JsonObject)x).get("trackId").getAsString();
//			
//			out.println("<tr>");
//			out.println("<td>" + "<a href=\"/artistinfo?artist=" + artist + "\">" + artist + "</a></td>");
//			out.println("<td>" + "<a href=\"/songinfo?trackId=" + trackId + "\">" + title + "</a></td>");
//			out.println("</tr>");
//		}
//		out.println("</table>");
//		out.println(footer());
//		
//	}

}
