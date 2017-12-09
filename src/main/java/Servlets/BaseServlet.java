package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Libraries.MyMusicLibrary;

/**
 * BaseServlet acts as parent to all other classes
 * Stores several protected methods to easily implement 
 * repetitive html outputs.
 * 
 * Stores certain final variables used for sessions
 * @author Rong
 *
 */
public class BaseServlet extends HttpServlet {

	public static final String LIBRARY = "library";
	public static final String ARTISTLIBRARY = "artistLibrary";
	public static final String USERLIBRARY = "userlibrary";
	public static final String USERNAME = "username";
	public static final String DISPLAYNAME = "displayName";
	public static final String LOG = "log";
	public static final String LOGGED_IN = "logged_in ";
	
	/*
	 * Prepare a response of HTML 200 - OK.
	 * Set the content type and status.
	 * Return the PrintWriter.
	 */
	protected PrintWriter prepareResponse(HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		return response.getWriter();
	}
	
	protected String debugger(String word) {
		return "<p>" + word + "</p>";
	}

	protected String header() {
		return "<html><style>";
	}
	
	protected String closeStyle() {
		return "</style>";
	}
	
	/*
	 * Return the beginning part of the HTML page.
	 */
	protected String body(String title) {
		return "<head><title>" + title + "</title></head><body>";		
	}

	protected String footer() {
		return "<footer><center> Copyright &copy; The Threads Music Company </center></footer>";
	}
	
	/*
	 * Return the last part of the HTML page. 
	 */
	protected String end() {
		return "</body></html>";
	}

	/*
	 * Given a request, return the value of the parameter with the
	 * provided name or null if none exists.
	 */
	protected String getParameterValue(HttpServletRequest request, String key) {
		return request.getParameter(key);
	}
	
}
