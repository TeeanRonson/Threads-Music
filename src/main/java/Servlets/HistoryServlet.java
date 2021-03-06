package Servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import Libraries.MyUserLibrary;

/**
 * Servlet that stores a specific users search history
 * 
 * Option to delete hisotry
 * @author Rong
 *
 */
public class HistoryServlet extends BaseServlet {
	
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
			out.println("<p><a href=\"/home\">Home</a> <a href=\"/artist\">All Artists</a> <a href=\"suggested\">Popular</a> <a href=\"history\"> History </a> <a align=\"right\" href=\"logout\">Log out</a></p>");			
			out.println("</form><div class=\"tfclear\"></div></div></p>");
			out.println("<h4> History </h4>");
			out.println("<table style=\"width:50%\" border=\"1\">");
			out.println("<thead><th> Searches </th></thead>");
			
			JsonObject history = uLibrary.getHistory(username);
			JsonArray result = history.get("history").getAsJsonArray();
			for (int i = result.size() - 1; i >= 0; i--) {
				String search = result.get(i).getAsString();
				out.println("<tr>");
				out.println("<td>" + search + "</td>");
				out.println("</tr>");
			}
			out.println("</table>");
			out.println("<p><form method=\"post\" action=\"history\"");
			out.println("<label> </label>");
			out.println("<input type=\"submit\" value=\"Clear history\" class=\"tfbutton\">");
			out.println("</form><div class=\"tfclear\"></div></div></p>");
			out.println(footer());
			out.println(end());
		}	
	}	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		MyUserLibrary uLibrary = (MyUserLibrary) getServletConfig().getServletContext().getAttribute(USERLIBRARY);
		
		if (request.getSession().getAttribute(LOGGED_IN) == null || (boolean) request.getSession().getAttribute(LOGGED_IN) == false) {
			response.sendRedirect("/mainmenu");
		} else { 
		
			String username = (String) request.getSession().getAttribute(USERNAME);
			
			uLibrary.clearHistory(username);
			
			response.sendRedirect("/history");
		}	
	}
	
}
