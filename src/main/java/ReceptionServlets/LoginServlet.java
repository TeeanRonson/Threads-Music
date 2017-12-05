package ReceptionServlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import BaseObjects.SingleUserInfo;
import Libraries.MyUserLibrary;
import Servlets.BaseServlet;

public class LoginServlet extends BaseServlet {
	

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		PrintWriter out = response.getWriter();
		
		out.println(header());
		out.println("input[type=text], input[type=password] { width: 100%; padding: 12px 20px; margin: 8px 0;display: inline-block;border: 1px solid #ccc; box-sizing: border-box }");
		out.println("button { background-color: #4CAF50; color: white; padding: 14px 20px; margin: 8px 0; border: none; cursor: pointer; width: 100%; }");
		out.println(".container { padding: 16px; }");
		out.println(".signupbtn { float: left; width: 100% }");
		out.println(".clearfix::after { content: \"\"; clear: both; display: table;}");
		out.println(closeStyle());
		out.println(body("ThreadsMusic"));
		out.println("<img src=\"http://i64.tinypic.com/jkvnlz.jpg\" height= \"75\" width=\"100\" style=\"float:left;\"/>");
		out.println("<h2>Login</h2>");
		out.println("<h5> Welcome back! </h5>");
		out.println("<form id=\"login\" action=\"login\" method=\"post\" style=\"border:1px solid #ccc\"> ");
		out.println("<div class=\"container\">");
		out.println("<label><b> Username </b></label>");
		out.println("<input type=\"text\" placeholder=\"Username\" name=\"username\" required>");
		out.println("<label><b> Password </b></label>");
		out.println("<input type=\"password\" placeholder=\"Password\" name=\"password\" required=\"\">");
		out.println("<div class=\"clearfix\">");
		out.println("<button type=\"submit\" class=\"signupbtn\">Login</button>");
		out.println("<form id=\"main\" action=\"mainmenu\" method=\"get\" style=\"border:1px solid #ccc\">");
		out.println("<div class=\"clearfix\">");
		out.println("<button type=\"mainmenu\" class=\"signupbtn\">Main Menu</button>");
		out.println("</form><div class=\"tfclear\"></div></div></p>");
		out.println(footer());
		out.println("</div></div></form>");
		out.println(end());
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		response.setStatus(HttpServletResponse.SC_OK);
		MyUserLibrary uLibrary = (MyUserLibrary) getServletConfig().getServletContext().getAttribute(USERLIBRARY);
		
		PrintWriter out = response.getWriter();
		
		String username = request.getParameter("username").toLowerCase();
		String password = request.getParameter("password").toLowerCase();
		String newLogin = LocalDateTime.now().toString();
		
		if (uLibrary.verifyUser(username, password) == true) {
			HttpSession session = request.getSession();
			session.setAttribute(LOGGED_IN, true);
			session.setAttribute(USERNAME, username);
			uLibrary.setNewLogin(username, newLogin);
			response.sendRedirect("/home");

	 	} else { 
	 		
	 		out.println(header());
			out.println("input[type=text], input[type=password] { width: 100%; padding: 12px 20px; margin: 8px 0;display: inline-block;border: 1px solid #ccc; box-sizing: border-box }");
			out.println("button { background-color: #4CAF50; color: white; padding: 14px 20px; margin: 8px 0; border: none; cursor: pointer; width: 100%; }");
			out.println(".container { padding: 16px; }");
			out.println(".signupbtn { float: left; width: 100% }");
			out.println(".clearfix::after { content: \"\"; clear: both; display: table;}");
			out.println(closeStyle());
			out.println(body("Login"));
			out.println("<img src=\"http://i64.tinypic.com/jkvnlz.jpg\" height= \"75\" width=\"100\" style=\"float:left;\"/>");
			out.println("<h3>We could not find your username and password!</h3>");
			out.println("<form id=\"createAccount\" action=\"newAccount\" method=\"get\" style=\"border:1px solid #ccc\">");
			out.println("<div class=\"clearfix\">");
			out.println("<button type=\"create\" class=\"signupbtn\">Sign Up</button>");
			out.println("</form><div class=\"tfclear\"></div></div></p>");
			out.println("<form id=\"main\" action=\"mainmenu\" method=\"get\" style=\"border:1px solid #ccc\">");
			out.println("<div class=\"clearfix\">");
			out.println("<button type=\"mainmenu\" class=\"signupbtn\">Main Menu</button>");
			out.println("</form><div class=\"tfclear\"></div></div></p>");
			out.println(end());
	 	}
	}
}
