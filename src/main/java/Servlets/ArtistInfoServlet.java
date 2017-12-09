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

/**
 * Servlet that displays saved information about a specific artist. 
 * Includes: 
 * Image
 * Name 
 * Listeners
 * PlayCount 
 * Biography 
 * Top Tracks by that Artist
 * @author Rong
 *
 */
public class ArtistInfoServlet extends BaseServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		if (request.getSession().getAttribute(LOGGED_IN) == null || (boolean) request.getSession().getAttribute(LOGGED_IN) == false) {
			response.sendRedirect("/mainmenu");
		} else { 
		
			MyArtistLibrary aLibrary = (MyArtistLibrary) getServletConfig().getServletContext().getAttribute(ARTISTLIBRARY);
			MyUserLibrary uLibrary = (MyUserLibrary) getServletConfig().getServletContext().getAttribute(USERLIBRARY);
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
			out.println("<input type=\"submit\" value=\"submit\" class=\"tfbutton\">");
			out.println("<p><a href=\"/home\">Home</a> <a href=\"/artist\">All Artists</a> <a href=\"suggested\">Popular</a> <a href=\"history\"> History </a> <a href=\"mainmenu\">Log out</a></p>");
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
	}
}
