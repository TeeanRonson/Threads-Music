package Servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import songfinder.MyLibrary;

public class ArtistServlet extends BaseServlet {
	
	BaseServlet bs = new BaseServlet();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		MyLibrary library = (MyLibrary) getServletConfig().getServletContext().getAttribute(LIBRARY);
		
		PrintWriter out = response.getWriter();
		
		out.println(bs.header("All Artists"));
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
		out.println("<p><a href=\"http://localhost:8080/artist\"><b>Artists</b></a></p>");				// All Artists Link 
		out.println("</form><div class=\"tfclear\"></div></div></p>");
		
		
		
		out.println("<table style=\"width:40%\" border=\"1\">");
		out.println("<thead><tr><th>Artists</th></tr></thead>");
		
		for (String x: library.getByArtist().keySet()) {
			out.println("<tr>");
			out.println("<td>" + x + "</td>");
			out.println("</tr>");	
		}
		out.println("</table>");
	}
}
