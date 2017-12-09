package ReceptionServlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Servlets.BaseServlet;

public class MainMenuServlet extends BaseServlet {
	
	/** 
	 * Servlet that navigates the user to either the 'Login' page or the 'Sign in' page
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		PrintWriter out = response.getWriter();
	
		out.println(header());
		out.println("button { background-color: #000000; color: white; padding: 14px 20px; margin: 8px 0; border: none; cursor: pointer; width: 100%; }");
		out.println(".loginbtn { padding: 14px 20px; background-color: #000000; }");
		out.println(".loginbtn.signupbtn { float: left; width: 50%; }");
		out.println(".container { padding: 16px; } ");
		out.println(".clearfix: after { content: \"\"; clear: both; display: table; }");
		out.println("img { display: block; margin: auto; }");
		out.println("#example1 { border: 1px solid black; background:url(http://tinypic.com?ref=f0dtag); background-repeat: no-repeat; padding:15px; }");
		out.println(closeStyle());
		out.println(body("ThreadsMusic"));
		out.println("<center>");
		out.println("<img src=\"http://i64.tinypic.com/jkvnlz.jpg\" height= \"550\" width=\"850\">");
		out.println("<form id=\"menu1\" action=\"login\" method=\"get\" style=\"border:1px solid #ccc\">	");
		out.println("<div class=\"clearfix\">");
		out.println("<button type=\"login\" class=\"loginbtn\">Login</button>");
		out.println("</form>");
		out.println("<form id=\"menu2\" action=\"newAccount\" method=\"get\" style=\"border:1px solid #ccc\">");
		out.println("<div class=\"clearfix\">");
		out.println("<button type=\"create\" class=\"signupbtn\">Sign Up</button>");
		out.println("</form><div class=\"tfclear\"></div></div></p>");
		out.println(footer());
		out.println(end());
	
		
	}	

}
