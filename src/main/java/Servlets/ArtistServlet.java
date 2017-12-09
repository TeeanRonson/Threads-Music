package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import BaseObjects.SingleArtistInfo;
import Libraries.MyArtistLibrary;
import Libraries.MyMusicLibrary;
import Libraries.MyUserLibrary;

public class ArtistServlet extends BaseServlet {
	
	/**
	 * Servlet that displays a list of All Artists
	 * Option to sort the artist alphabetically or by play count
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		MyUserLibrary uLibrary = (MyUserLibrary) getServletConfig().getServletContext().getAttribute(USERLIBRARY);
		
		if (request.getSession().getAttribute(LOGGED_IN) == null || (boolean) request.getSession().getAttribute(LOGGED_IN) == false) {
			response.sendRedirect("/mainmenu");
		} else {
		
			MyArtistLibrary aLibrary = (MyArtistLibrary) getServletConfig().getServletContext().getAttribute(ARTISTLIBRARY);
			String username = (String) request.getSession().getAttribute(USERNAME);
			String displayName = (String) request.getSession().getAttribute(DISPLAYNAME);
			String lastLogin = uLibrary.getLastLogin(username);	
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
	
			PrintWriter out = response.getWriter();
			
			out.println(body("All Artists"));
			out.println("<center>");
			out.println("<img src=\"http://i64.tinypic.com/jkvnlz.jpg\" height= \"75\" width=\"100\" style=\"float:middle;\"/>");
			out.println("<p>Search for an artist, song title or popular genre! </p>");
			out.println("<p>" + displayName + "</p>");
			out.println("<p> Last login: " + lastLogin + "</p>");
			out.println("<hr/>");
			out.println("<p><form method=\"post\" action=\"artist\"");
			out.println("<label>Search type:</label>");
			out.println("<select options: \"choices\" name=\"type\">");
			out.println("<option value = \"artist\">Artist</option> <option value = \"title\">Title</option> <option value = \"tag\">Tag</option>");
			out.println("</select>");
			out.println("<label>Query: </label>");
			out.println("<input type=\"text\" class=\"tftextinput\" name=\"query\" size=\"21\" maxlength=\"60\">");
			out.println("<input type=\"submit\" value=\"submit\" class=\"tfbutton\">");
			out.println("<p><a href=\"/home\">Home</a> <a href=\"/artist\">All Artists</a> <a href=\"suggested\">Popular</a> <a href=\"history\"> History </a> <a href=\"logout\">Log out</a></p>");
			out.println("<label>Sorted by:</label>");
			out.println("<select options: \"sortedSelection\" name=\"sort\">");
			out.println("<option value = \"byArtist\">Artist</option> <option value = \"byPlayCount\">Playcount</option>");
			out.println("<input type=\"submit\" value=\"submit\" class=\"tfbutton\">"); 
			out.println("</select>");
			out.println("</form><div class=\"tfclear\"></div></div></p>");
			out.println("<table style=\"width:40%\" border=\"1\">");
			out.println("<thead><tr><th>Artists</th><th> Playcount </th></tr></thead>");
			
			JsonArray listOfArtists = aLibrary.displayArtists();
			for (JsonElement x: listOfArtists) {
				String artistName = ((JsonObject)x).get("artist").getAsString();
				Integer playCount = ((JsonObject)x).get("playCount").getAsInt();
				String encodeA = URLEncoder.encode(artistName, "UTF-8");
				out.println("<tr>");
				out.println("<td>" + "<a href=\"/artistinfo?artist=" + encodeA + "\">" + artistName + "</a></td>");
				out.println("<td>" + playCount + "</td>");
				out.println("</tr>");	
			}
			out.println("</table>");
			out.println(footer());
			out.println(end());
		}	
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
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
			out.println("</form>");
			out.println("<p><a href=\"/home\">Home</a> <a href=\"/artist\">All Artists</a> <a href=\"suggested\">Popular</a> <a href=\"history\"> History </a> <a align=\"right\" href=\"logout\">Log out</a></p>");
			out.println("<p><form method=\"post\" action=\"artist\"");
			out.println("<label>Sorted by:</label>");
			out.println("<select options: \"sortedSelection\" name=\"sort\">");
			out.println("<option value = \"byArtist\">Artist</option> <option value = \"byPlayCount\">Playcount</option>");
			out.println("<input type=\"submit\" value=\"submit\" class=\"tfbutton\">"); 
			out.println("</select>");
			out.println("</form><div class=\"tfclear\"></div></div></p>");
			out.println("<table style=\"width:50%\" border=\"1\">");
			out.println("<thead><tr><th>Artist</th><th> Playcount </th></tr></thead>");
			
			String sort = request.getParameter("sort");
			
			System.out.println(sort);
		
			if (sort.equals("byArtist")) {
				JsonArray listOfArtists = aLibrary.displayArtists();
				for (JsonElement x: listOfArtists) {
					String artistName = ((JsonObject)x).get("artist").getAsString();
					Integer playCount = ((JsonObject)x).get("playCount").getAsInt();
					String encodeA = URLEncoder.encode(artistName, "UTF-8");
					out.println("<tr>");
					out.println("<td>" + "<a href=\"/artistinfo?artist=" + encodeA + "\">" + artistName + "</a></td>");
					out.println("<td>" + playCount + "</td>");
					out.println("</tr>");	
				}
			}
			
			if (sort.equals("byPlayCount")) {
				JsonArray result = aLibrary.displayByPlayCount();
				for (JsonElement x: result) {
					String artistName = ((JsonObject)x).get("artist").getAsString();
					Integer playCount = ((JsonObject)x).get("playCount").getAsInt();
					String encodeA = URLEncoder.encode(artistName, "UTF-8");
					out.println("<tr>");
					out.println("<td>" + "<a href=\"/artistinfo?artist=" + encodeA + "\">" + artistName + "</a></td>");
					out.println("<td>" + playCount + "</td>");
					out.println("</tr>");	
				}
			}
			
			out.println("</table>");
			out.println(footer());
			out.println(end());
		}	
	}
	
}
