package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Libraries.MyArtistLibrary;
import Libraries.MyMusicLibrary;
import Libraries.MyUserLibrary;
import songfinder.LibraryBuilder;

public class HomeServlet extends BaseServlet {
	
	
	/** 
	 * Servlet that acts as the home page of the Threads Music web application 
	 * 
	 * Allows a user to search for an artist, title or tag. 
	 * If an artist is searched, an image of the artist, top tracks by the Artist and similar songs will be returned.
	 * If a title is searched, different versions of the song and similar songs are returned. 
	 * If a tag is searched, all songs related to the tag will be shown. 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		if (request.getSession().getAttribute(LOGGED_IN) == null || (boolean) request.getSession().getAttribute(LOGGED_IN) == false) {
			response.sendRedirect("/mainmenu");
		} else {
		
			MyUserLibrary uLibrary = (MyUserLibrary) getServletConfig().getServletContext().getAttribute(USERLIBRARY);
			String username = (String) request.getSession().getAttribute(USERNAME);
			String displayName = (String) request.getSession().getAttribute(DISPLAYNAME);
			String lastLogin = uLibrary.getLastLogin(username);		
			
			
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
			
			PrintWriter out = response.getWriter();
			
			out.println(header());
			out.println("body { background image: url(\"http://i68.tinypic.com/2sb1izm.jpg\" border=\"0\" alt=\"Image and video hosting by TinyPic\"");
			out.println(closeStyle());
			out.println(body("Threads Music"));
			out.println("<center>");
			out.println("<img src=\"http://i64.tinypic.com/jkvnlz.jpg\" height=\"75\" width=\"100\" style=\"float:middle;\"/>");
			out.println("<p>Search for an artist, song title or popular genre! </p>");
			out.println("<p>" + displayName + "</p>");
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
			out.println("<p><a href=\"/home\">Home</a> <a href=\"/artist\">All Artists</a>  <a href=\"suggested\">Popular</a> <a href=\"history\">History</a> <a align=\"right\" href=\"logout\">Log out</a></p>");			
			out.println("</form><div class=\"tfclear\"></div></div></p>");
			out.println(footer());
			out.println(end());
		}	
	}
			
		
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		MyMusicLibrary mLibrary = (MyMusicLibrary) getServletConfig().getServletContext().getAttribute(LIBRARY);
		MyArtistLibrary aLibrary = (MyArtistLibrary) getServletConfig().getServletContext().getAttribute(ARTISTLIBRARY);
		MyUserLibrary uLibrary = (MyUserLibrary) getServletConfig().getServletContext().getAttribute(USERLIBRARY);

		if (request.getSession().getAttribute(LOGGED_IN) == null || (boolean) request.getSession().getAttribute(LOGGED_IN) == false) {
			response.sendRedirect("/mainmenu");
		}
		String username = (String) request.getSession().getAttribute(USERNAME);
		String displayName = (String) request.getSession().getAttribute(DISPLAYNAME);
		String lastLogin = uLibrary.getLastLogin(username);
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		PrintWriter out = response.getWriter();
	
		out.println(body("Threads Music"));
		out.println("<center>");
		out.println("<img src=\"http://i64.tinypic.com/jkvnlz.jpg\" height= \"75\" width=\"100\" style=\"float:middle;\"/>");
		out.println("<p>Search for an artist, song title or popular genre! </p>");
		out.println("<p>" + displayName + "</p>");
		out.println("<p> Last login: " + lastLogin + "</p>");
		out.println("<hr/>");
		out.println("<p><form method=\"post\" action=\"home\"");
		out.println("<label>Search type:</label>");
		out.println("<select options:\"choices\" name=\"type\">");
		out.println("<option value =\"artist\">Artist</option><option value=\"title\">Title</option><option value =\"tag\">Tag</option>");
		out.println("</select>");
		out.println("<label>Query: </label>");
		out.println("<input type=\"text\" class=\"tftextinput\" name=\"query\" size=\"21\" maxlength=\"60\">");
		out.println("<input type=\"submit\" value=\"Submit\" class=\"tfbutton\">");
		out.println("<p><a href=\"/home\">Home</a> <a href=\"/artist\">All Artists</a>  <a href=\"suggested\">Popular</a> <a href=\"history\">History</a> <a align=\"right\" href=\"logout\">Log out</a></p>");
		out.println("</form><div class=\"tfclear\"></div></div></p>");
	
		String type = request.getParameter("type");
		String originalQuery = request.getParameter("query");
		String query = originalQuery.toLowerCase();
		
		if (query.equals(null)) {
			response.sendRedirect("/home");
		}
	
		uLibrary.addToHistory(username, originalQuery);
		
		if (type.equals("artist")) {			
			JsonObject dummy = mLibrary.caseCheckArtist(query, "artist"); 
			if (dummy.get("artist") != null) {
				String name = dummy.get("artist").getAsString();
				uLibrary.addPopularArtist(name);
				JsonArray result = aLibrary.getTopTracks(name);
				
				JsonObject artistInfo = aLibrary.getArtistInfo(name);
				String image = artistInfo.get("image").getAsString();
				
				out.println("<img src=\"" + image + "\" alt=\"artistImage\" width=\"500\" height=\"377\">");
				out.println("<h4>Top Results</h4>");
				out.println("<table style=\"width:50%\" border=\"1\">");
				out.println("<thead><th> Songs </th><th> </th></thead>");
	
				for (JsonElement x: result) {
					String trackTitle = ((JsonObject)x).get("name").getAsString();
					String url = ((JsonObject)x).get("url").getAsString();
					out.println("<tr>");
					out.println("<td>" + trackTitle + "</a></td>");
					out.println("<td>" + "<a href=\"" + url + "\">" + "Listen" + "</a></td>");
					out.println("</tr>");
				}
				out.println("</table>");
			}
		}	
		
		if (type.equals("title")) {
			JsonObject dummy = mLibrary.caseCheckTitle(query, "title");
			if (dummy.get("title") != null) {
				String title = dummy.get("title").getAsString();
				uLibrary.addPopularTitle(title);
				JsonArray result = mLibrary.getTitleSongs(title);
				
				out.println("<h4>Top Results</h4>");
				out.println("<table style=\"width:50%\" border=\"1\">");
				out.println("<thead><th> Songs </th><th> </th></thead>");
	
				for (JsonElement x: result) {
					String artistName = ((JsonObject)x).get("artist").getAsString();
					String trackTitle = ((JsonObject)x).get("title").getAsString();
					String trackId = ((JsonObject)x).get("trackId").getAsString();
					
					out.println("<tr>");
					out.println("<td>" + "<a href=\"/songinfo?trackId=" + trackId + "\">" + trackTitle + "</a></td>");
					out.println("<td>" + "<a href=\"/artistinfo?artist=" + artistName + "\">" + artistName + "</a></td>");
					out.println("</tr>");	
				}
				out.println("</table>");
			}	
		}
		
		out.println("<h4> Related Songs </h4>");
		out.println("<table style=\"width:50%\" border=\"1\">");
		out.println("<thead><tr><th>Artist</th><th>Song Title</th></tr></thead>");
		System.out.println("This is the type:" + type);
		System.out.println("This is the query:" + query);
		
		JsonArray similars = new JsonArray();
		JsonObject object = new JsonObject();
		
		if (type.equals("artist")) {
			object = mLibrary.caseCheckArtist(query, "artist");
			System.out.println(object);
			System.out.println("hello");
			if (object.equals(null)) {
				System.out.println("did we get here");
				object = mLibrary.partialSearch(query, "artist");
				
			}
		}	

		if (type.equals("title")) {
			object = mLibrary.caseCheckTitle(query, "title");
			if (object.equals(null)) {
				object = mLibrary.partialSearch(query, "title");
			}
		}
		
		if (type.equals("tag")) {
			object = mLibrary.caseCheckTag(query);
			if (object.equals(null)) {
				object = mLibrary.partialSearch(query, "tag");
			}
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
