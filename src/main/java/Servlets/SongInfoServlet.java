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

/**
 * Servlet that displays saved information about a specific song. 
 * Includes: 
 * Artist 
 * Title 
 * Similar Songs 
 * @author Rong
 *
 */
public class SongInfoServlet extends BaseServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		if (request.getSession().getAttribute(LOGGED_IN) == null || (boolean) request.getSession().getAttribute(LOGGED_IN) == false) {
			response.sendRedirect("/mainmenu");
		} else { 
		
			MyMusicLibrary mlibrary = (MyMusicLibrary) getServletConfig().getServletContext().getAttribute(LIBRARY);
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
			out.println("<p> Search for an artist, song title or popular genre! </p>");
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
			out.println("<p><a href=\"/home\">Home</a> <a href=\"/artist\">All Artists</a> <a href=\"suggested\">Popular</a> <a href=\"history\"> History </a> <a href=\"mainmenu\">Log out</a></p>");			
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
	}

}
