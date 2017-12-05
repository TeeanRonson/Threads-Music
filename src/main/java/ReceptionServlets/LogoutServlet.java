package ReceptionServlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Libraries.MyUserLibrary;
import Servlets.BaseServlet;

public class LogoutServlet extends BaseServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		MyUserLibrary uLibrary = (MyUserLibrary) getServletConfig().getServletContext().getAttribute(USERLIBRARY);
		
		HttpSession session = request.getSession();
		session.setAttribute(LOGGED_IN, false);
		
		String username = (String) request.getSession().getAttribute(USERNAME);
		uLibrary.setLastLogin(username);
		session.removeAttribute(USERNAME);
	
		response.sendRedirect("/mainmenu");
		
	}	

}
