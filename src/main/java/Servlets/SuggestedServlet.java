package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;

import Libraries.MyUserLibrary;

public class SuggestedServlet extends BaseServlet {
	
	/**
	 * Servlet that displays the top 10 artist and title searches by all users on the platform. 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		MyUserLibrary uLibrary = (MyUserLibrary) getServletConfig().getServletContext().getAttribute(USERLIBRARY);
		
		if (request.getSession().getAttribute(LOGGED_IN) == null || (boolean) request.getSession().getAttribute(LOGGED_IN) == false) {
			response.sendRedirect("/mainmenu");
		} else { 
		
			String username = (String) request.getSession().getAttribute(USERNAME);
			String displayName = (String) request.getSession().getAttribute(DISPLAYNAME);
			String lastLogin = uLibrary.getLastLogin(username);	
			
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
			
			PrintWriter out = response.getWriter();
			
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
			out.println("<p><a href=\"/home\">Home</a> <a href=\"/artist\">All Artists</a> <a href=\"suggested\">Popular</a> <a href=\"history\">History</a> <a align=\"right\" href=\"logout\">Log out</a></p>");			
			out.println("</form><div class=\"tfclear\"></div></div></p>");
			out.println("<h4> Trending </h4>");
			out.println("<table style=\"width:50%\" border=\"1\">");
			out.println("<thead><th> Popular Artists </th></thead>");
			
			JsonArray result = uLibrary.getPopularArtists();
			
			for (int i = 0; i < result.size(); i++) {
				String artistName = result.get(i).getAsString();
				String encodeA = URLEncoder.encode(artistName, "UTF-8");
				out.println("<tr>");
				out.println("<td>" + "<a href=\"/artistinfo?artist=" + encodeA + "\">" + artistName + "</a></td>");
				out.println("</tr>");
			}
			
			out.println("</table>");
			out.println("<h6> </h6>");
			out.println("<table style=\"width:50%\" border=\"1\">");
			out.println("<thead><th> Popular Titles </th></thead>");
			
			JsonArray result1 = uLibrary.getPopularTitles();
			
			for (int i = 0; i < result1.size(); i++) {
				String title = result1.get(i).getAsString();
				out.println("<tr>");
				out.println("<td>" + title + "</td>");
				out.println("</tr>");
			}
			out.println("</table>");
			out.println(footer());
			out.println(end());
		}
	}	
}
