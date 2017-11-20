package Servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import songfinder.LibraryBuilder;
import songfinder.MyLibrary;

public class LibraryServlet extends BaseServlet {
	
	MyLibrary myLibrary = new MyLibrary();
	LibraryBuilder buildLibrary = new LibraryBuilder();
	BaseServlet bs = new BaseServlet();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		MyLibrary music = (MyLibrary) getServletConfig().getServletContext().getAttribute(LIBRARY);
		
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		HttpSession session = request.getSession();
		
		JsonArray output = new JsonArray();
		JsonObject object = new JsonObject();
		String type = request.getParameter("type");
		String query = request.getParameter("query");
		String artist = "Artist";
		String title = "Title";
		String tag = "Tag";
		
		PrintWriter out = bs.prepareResponse(response);
		
		out.println(bs.header("SongFinder"));
		out.println("<h1>Song Finder</h1>");
		out.println("<p>Welcome to SongFinder! Search for an artist, song title or tag and we will give you a list of similar songs you might like. </p>");
		out.println("<hr/>");
		out.println("<p><form id=\"newsearch\" method=\"get\" action=\"\"");
		out.println(" <label>Search type:</label>");
		out.println("<select options: \"choices\" name=\"type\">");
		out.println("<option value = \"artist\">Artist</option> <option value = \"title\">Title</option> <option value = \"tag\">Tag</option>");
		out.println("</select>");
		out.println("<label>Query: </label>");
		out.println("<input type=\"text\" class=\"tftextinput\" name=\"q\" size=\"21\" maxlength=\"60\">");
		out.println("<input type=\"submit\" value=\"submit\" class=\"tfbutton\">");
		out.println("</form><div class=\"tfclear\"></div></div></p>");
		out.println("<p>" + type + "</p>");
//		out.println(query);
		
		if (type.equals(artist)) {
			object = myLibrary.searchByArtist(query);
		}	

		if (type.equals(title)) {
			object = myLibrary.searchByTitle(query);
		}
		
		if (type.equals(tag)) {
			object = myLibrary.searchByTag(query);
		}
		
		output = object.getAsJsonArray("similars");
		
		for (JsonElement x: output) {
			out.write("<table border=\"1\">");
			out.write("<tr>");
			out.write("<td>" + ((JsonObject)x).get(artist).getAsString() + "</td>");
			out.write("<td>" + ((JsonObject)x).get(title).getAsString() + "</td>");
			out.write("</tr></table>");
		}
		
		out.println(bs.footer());
	}
}
