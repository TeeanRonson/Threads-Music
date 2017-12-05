package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import BaseObjects.SingleArtistInfo;
import BaseObjects.SingleSongInfo;
import Libraries.MyArtistLibrary;
import Libraries.MyMusicLibrary;
import Libraries.MyUserLibrary;

public class ArtistInfoServlet extends BaseServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		if (request.getSession().getAttribute(LOGGED_IN) == null || (boolean) request.getSession().getAttribute(LOGGED_IN) == false) {
			response.sendRedirect("/mainmenu");
		} 
		
		MyArtistLibrary aLibrary = (MyArtistLibrary) getServletConfig().getServletContext().getAttribute(ARTISTLIBRARY);
		MyUserLibrary uLibrary = (MyUserLibrary) getServletConfig().getServletContext().getAttribute(USERLIBRARY);
		String username = (String) request.getSession().getAttribute(USERNAME);
		String lastLogin = uLibrary.getLastLogin(username);		
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		PrintWriter out = response.getWriter();
	
		out.println(body("Threads Music"));
		out.println("<center>");
		out.println("<img src=\"http://i64.tinypic.com/jkvnlz.jpg\" height= \"75\" width=\"100\" style=\"float:middle;\"/>");
		out.println("<p>Search for an artist, song title or popular genre! </p>");
		out.println("<p>" + username + "</p>");
		out.println("<p> Last login: " + lastLogin + "</p>");
		out.println("<hr/>");
		out.println("<p><form method=\"post\" action=\"home\"");
		out.println("<label>Search type:</label>");
		out.println("<select options:\"choices\" name=\"type\">");
		out.println("<option value =\"artist\">Artist</option><option value=\"title\">Title</option><option value =\"tag\">Tag</option>");
		out.println("</select>");
		out.println("<label>Query: </label>");
		out.println("<input type=\"text\" class=\"tftextinput\" name=\"query\" size=\"21\" maxlength=\"60\">");
		out.println("<input type=\"submit\" value=\"submit\" class=\"tfbutton\">");
		out.println("<p><a href=\"/home\">Home</a> <a href=\"/artist\">All Artists</a> <a href=\"history\"> History </a> <a href=\"mainmenu\">Log out</a></p>");
		out.println("</form><div class=\"tfclear\"></div></div></p>");
		
		String artist = request.getParameter("artist");
		JsonObject result = aLibrary.getArtistInfo(artist);
		
		String image = result.get("image").getAsString();
		String artistName = result.get("artist").getAsString();
		Integer listeners = result.get("listeners").getAsInt();
		Integer playCount = result.get("playCount").getAsInt();
		String bio = result.get("bio").getAsString();
		
		out.println("<img src=\"" + image + "\" alt=\"artistImage\" width=\"500\" height=\"377\">");
		out.println("<p>Artist: " + artistName + "</p>");
		out.println("<p>Listeners: " + listeners + "</p>");
		out.println("<p>Play count: " + playCount + "</p>");
		out.println("<p>Biography: " + bio + "</p>");
		out.println("<table style=\"width:50%\" border=\"1\">");
		out.println("<thead><th> Top Tracks </th></tr></thead>");
		
		JsonArray topTracksArray = aLibrary.getTopTracks(artist);
		for (JsonElement x: topTracksArray) {
			String track = ((JsonObject)x).get("name").getAsString();
			String url = ((JsonObject)x).get("url").getAsString();
			out.println("<tr>");
			out.println("<td>" + track + "</a></td>");
			out.println("<td>" + "<a href=\"" + url + "\">" + "Listen" + "</a></td>");
			out.println("</tr>");
		}
		out.println("</table>");
		out.println(end());
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

		if (request.getSession().getAttribute(LOGGED_IN) == null || (boolean) request.getSession().getAttribute(LOGGED_IN) == false) {
			response.sendRedirect("/mainmenu");
		}
			
		
		MyMusicLibrary mLibrary = (MyMusicLibrary) getServletConfig().getServletContext().getAttribute(LIBRARY);
		MyArtistLibrary aLibrary = (MyArtistLibrary) getServletConfig().getServletContext().getAttribute(ARTISTLIBRARY);
		MyUserLibrary uLibrary = (MyUserLibrary) getServletConfig().getServletContext().getAttribute(USERLIBRARY);
		String username = (String) request.getSession().getAttribute(USERNAME);
		String lastLogin = uLibrary.getLastLogin(username);
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		PrintWriter out = response.getWriter();
	
		out.println(body("Threads Music"));
		out.println("<img src=\"http://i64.tinypic.com/jkvnlz.jpg\" height= \"75\" width=\"100\" style=\"float:middle;\"/>");
		out.println("<p>Search for an artist, song title or popular genre! </p>");
		out.println("<p>" + username + "</p>");
		out.println("<p> Last login: " + lastLogin + "</p>");
		out.println("<hr/>");
		out.println("<p><form method=\"post\" action=\"home\"");
		out.println("<label>Search type:</label>");
		out.println("<select options:\"choices\" name=\"type\">");
		out.println("<option value =\"artist\">Artist</option><option value=\"title\">Title</option><option value =\"tag\">Tag</option>");
		out.println("</select>");
		out.println("<label>Query: </label>");
		out.println("<input type=\"text\" class=\"tftextinput\" name=\"query\" size=\"21\" maxlength=\"60\">");
		out.println("<input type=\"submit\" value=\"submit\" class=\"tfbutton\">");
		out.println("<p><a href=\"/home\">Home</a> <a href=\"/artist\">All Artists</a> <a href=\"history\"> History </a> <a align=\"right\" href=\"logout\">Log out</a></p>");
		out.println("</form><div class=\"tfclear\"></div></div></p>");
		out.println("<h4>Tracks</h4>");
		out.println("<table style=\"width:50%\" border=\"1\">");
		out.println("<thead><th> Tracks </th></tr></thead>");
		
		JsonArray similars = new JsonArray();
		JsonObject object = new JsonObject();
		String type = request.getParameter("type");
		String query = request.getParameter("query").toLowerCase();
		if (query.equals(null)) {
			response.sendRedirect("/home");
		}
		
		if (type.equals("artist")) {
			
			JsonObject dummy = mLibrary.caseCheckArtist(query, "artist"); // we do this because of case checking - maybe create case check in Artist Library
			String name = dummy.get("artist").getAsString();
			for (JsonElement x: aLibrary.getTopTracks(name)) {
				String trackTitle = ((JsonObject)x).get("name").getAsString();
				String url = ((JsonObject)x).get("url").getAsString();
				
				out.println("<tr>");
				out.println("<td>" + trackTitle + "</a></td>");
				out.println("<td>" + "<a href=\"" + url + "\">" + "Play" + "</a></td>");
				out.println("</tr>");
			}
			
		} 
		
		if (type.equals("title")) {
			JsonObject dummy = mLibrary.caseCheckTitle(query, "title");
			String title = dummy.get("title").getAsString();
			JsonArray result = mLibrary.getTitleSongs(title);
			for (JsonElement x: result) {
				String trackTitle = ((JsonObject)x).get("title").getAsString();
				String trackId = ((JsonObject)x).get("trackId").getAsString();
				
				out.println("<tr>");
				out.println("<td>" + "<a href=\"/songinfo?trackId=" + trackId + "\">" + trackTitle + "</a></td>");
				out.println("</tr>");	
			}
		}
		
		out.println("</table>");
		out.println("<h4>Similar Songs</h4>");
		out.println("<table style=\"width:50%\" border=\"1\">");
		out.println("<thead><tr><th>Artist</th><th>Song Title</th></tr></thead>");
		System.out.println("This is the type:" + type);
		System.out.println("this is the query:" + query);
		
		if (type.equals("artist")) {
			object = mLibrary.caseCheckArtist(query, "artist");
		}	

		if (type.equals("title")) {
			object = mLibrary.caseCheckTitle(query, "title");
		}
		
		if (type.equals("tag")) {
			object = mLibrary.caseCheckTag(query);
		}
		
		if (object.get("similars") != null) {
			similars = object.get("similars").getAsJsonArray();
		}	
		
		
		for (JsonElement x: similars) {
			String artist = ((JsonObject)x).get("artist").getAsString();
			String title = ((JsonObject)x).get("title").getAsString();
			String trackId = ((JsonObject)x).get("trackId").getAsString();
			
			out.println("<tr>");
			out.println("<td>" + "<a href=\"/artistinfo?artist=" + artist + "\">" + artist + "</a></td>");
			out.println("<td>" + "<a href=\"/songinfo?trackId=" + trackId + "\">" + title + "</a></td>");
			out.println("</tr>");
		}
		out.println("</table>");
		out.println(footer());
		out.println(end());
	}
}
