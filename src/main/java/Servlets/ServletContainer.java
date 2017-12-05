package Servlets;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;

import Libraries.MyArtistLibrary;
import Libraries.MyMusicLibrary;
import Libraries.MyUserLibrary;
import ReceptionServlets.LoginServlet;
import ReceptionServlets.LogoutServlet;
import ReceptionServlets.MainMenuServlet;
import ReceptionServlets.NewAccountServlet;
import songfinder.LibraryBuilder;

public class ServletContainer {
	
	public static void main(String[] args) throws Exception {
		
		Server server = new Server(8060);
        LibraryBuilder builder = new LibraryBuilder();
        MyMusicLibrary mlibrary = builder.buildMusicLibrary("input", 35);
        MyArtistLibrary aLibrary = builder.buildArtistLibrary("input", 35);
        MyUserLibrary uLibrary = new MyUserLibrary();
        
        ServletContextHandler servletHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);        
        server.setHandler(servletHandler);

        servletHandler.addEventListener(new ServletContextListener() {

	    		public void contextDestroyed(ServletContextEvent sce) {
			}
	    		
			public void contextInitialized(ServletContextEvent sce) {
				sce.getServletContext().setAttribute(BaseServlet.LIBRARY, mlibrary);
				sce.getServletContext().setAttribute(BaseServlet.ARTISTLIBRARY, aLibrary);
				sce.getServletContext().setAttribute(BaseServlet.USERLIBRARY, uLibrary);
			}
        });

        servletHandler.addServlet(MainMenuServlet.class, "/mainmenu");
        servletHandler.addServlet(LoginServlet.class, "/login");
        servletHandler.addServlet(NewAccountServlet.class, "/newAccount");
        servletHandler.addServlet(LogoutServlet.class, "/logout");
        servletHandler.addServlet(HomeServlet.class, "/home");
        servletHandler.addServlet(ArtistServlet.class, "/artist");
        servletHandler.addServlet(ArtistInfoServlet.class, "/artistinfo");
        servletHandler.addServlet(SongInfoServlet.class, "/songinfo");
        servletHandler.addServlet(HistoryServlet.class, "/history");
        
        
        server.setHandler(servletHandler);
        server.start();
        server.join();
        // The use of server.join() will make the current thread join and
        // wait until the server is done executing.
        // See http://docs.oracle.com/javase/7/docs/api/java/lang/Thread.html#join()
	}
}
