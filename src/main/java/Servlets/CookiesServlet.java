package Servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookiesServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
	
		
		Cookie[] cookies = request.getCookies();
		int visits = 0;
		
		for (Cookie c: cookies) {
			System.out.println("Cookie - Name: " + c.getName() + " Value: " + c.getValue());
			if (c.getName().equals("visits")) {
				try { 
					visits = Integer.parseInt(c.getValue());
				
				} catch (NumberFormatException nfe) {
					nfe.getMessage();
					System.out.println("Caught exception!");
				}
			}		
		}
		
		response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        
        String visitsString = String.valueOf(++visits);
        response.addCookie(new Cookie("visits", visitsString));
        
        PrintWriter out = response.getWriter();
        out.println("<html><title>CookieServlet</title><body>There have been " + 
				visits + " visits.</body></html>");
        
        
	}

}
