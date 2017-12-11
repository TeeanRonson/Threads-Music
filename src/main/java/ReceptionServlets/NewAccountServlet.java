package ReceptionServlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import BaseObjects.SingleUserInfo;
import Libraries.MyUserLibrary;
import Servlets.BaseServlet;

public class NewAccountServlet extends BaseServlet {
	
	/** 
	 * Servlet that allows a new user to create an account with Threads Music. 
	 * 
	 * When a new account is created, a new SinlgeUserInfo object is created.
	 * SingleUserInfo object stores information related to the user.  
	 * 
	 * https://www.w3schools.com/
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
	
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		PrintWriter out = response.getWriter();
		
		out.println(header());
		out.println("input[type=text], input[type=password] { width: 100%; padding: 12px 20px; margin: 8px 0;display: inline-block;border: 1px solid #ccc; box-sizing: border-box }");
		out.println("button { background-color: #000000; color: white; padding: 14px 20px; margin: 8px 0; border: none; cursor: pointer; width: 100%; }");
		out.println(".cancelbtn { padding: 14 px 20px; background-color: #000000; }");
		out.println(".cancelbtn,.signupbtn { float:left; width 50%; }");
		out.println(".container { padding: 16px; } ");
		out.println("clearfix: after { content: \"\"; clear: both; display: table: }");
		out.println(closeStyle());
		out.println(body("ThreadsMusic"));
		out.println("<img src=\"http://i64.tinypic.com/jkvnlz.jpg\" height= \"75\" width=\"100\" style=\"float:left;\"/>");
		out.println("<h2> Sign Up </h2>");
		out.println("<h5> Sign Up for free and enjoy the music! </h5>");
		out.println("<form id=\"newAccount\" action=\"newAccount\" method=\"post\" style=\"border:1px solid #ccc\">");
		out.println("<div class=\"container\">");
		out.println("<label><b> Username </b></label>");
		out.println("<input type=\"text\" placeholder=\"Username\" name=\"username\" required>");
		out.println("<label><b> Password </b></label>");
		out.println("<input type=\"password\" placeholder=\"Password\" name=\"password\" required>");
		out.println("<label><b>Confirm Password</b></label>");
		out.println("<input type=\"password\" placeholder=\"Confirm Your Password\" name=\"confirmPassword\" required>");
		out.println("<div class=\"clearfix\">");
		out.println("<button type=\"submit\" class=\"signupbtn\"> Sign Up </button>");
		out.println("<form id=\"main\" action=\"mainmenu\" method=\"get\" style=\"border:1px solid #ccc\">");
		out.println("<div class=\"clearfix\">");
		out.println("<button type=\"mainmenu\" class=\"signupbtn\">Main Menu</button>");
		out.println("</form><div class=\"tfclear\"></div></div></p>");
		out.println(footer());
		out.println("</div></div></form>");
		out.println(end());
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		MyUserLibrary uLibrary = (MyUserLibrary) getServletConfig().getServletContext().getAttribute(USERLIBRARY);
		SingleUserInfo sui = null;

		String username = request.getParameter("username").toLowerCase();
		String displayName = request.getParameter("username");
		String password = request.getParameter("password").toLowerCase();
		String newLogin = LocalDateTime.now().toString();
		String lastLogin = "-";
		LinkedList<String> history = new LinkedList<String>();
		
		if (username != null && password != null) {
			sui = new SingleUserInfo(username, password, history, newLogin, lastLogin);
			uLibrary.addNewUser(sui);
		
			HttpSession session = request.getSession();
			session.setAttribute(LOGGED_IN, true);
			session.setAttribute(USERNAME, username);
			session.setAttribute(DISPLAYNAME, displayName);
			
			response.sendRedirect("/home");
		}	
	}
		
}
