package Servlets;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;

import songfinder.LibraryBuilder;
import songfinder.MyLibrary;

public class ServletContainer {
	
	public static void main(String[] args) throws Exception {
		
		Server server = new Server(8080);
        LibraryBuilder builder = new LibraryBuilder();
        MyLibrary library = builder.buildLibrary("input", 10);

        ServletContextHandler servletHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);        
        server.setHandler(servletHandler);

        servletHandler.addEventListener(new ServletContextListener() {

	    		public void contextDestroyed(ServletContextEvent sce) {
				// TODO Auto-generated method stub
				
			}
	    		
			public void contextInitialized(ServletContextEvent sce) {
				sce.getServletContext().setAttribute(BaseServlet.LIBRARY, library);
			
			}
    	
        });

        servletHandler.addServlet(HomeServlet.class, "/home");
        servletHandler.addServlet(ArtistServlet.class, "/artist");
        
        server.setHandler(servletHandler);
        server.start();
        server.join();
        // The use of server.join() will make the current thread join and
        // wait until the server is done executing.
        // See http://docs.oracle.com/javase/7/docs/api/java/lang/Thread.html#join()
       
	}
}
