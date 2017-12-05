package Sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class HttpFetcher {

	public static int PORT = 80;

	public static String download(String host, String path) {
		
		StringBuffer buffer = new StringBuffer();
		
		try (
				Socket sock = new Socket(host, PORT); 			//creates a connection to the web server
				OutputStream out = sock.getOutputStream(); 		//get the output stream from socket
				InputStream instream = sock.getInputStream();		//get the input stream from socket
																//wrap the input stream to make it easier to read from
				BufferedReader reader = new BufferedReader(new InputStreamReader(instream))
		) { 
		

			//Send request
			String request = getRequest(host, path);
			out.write(request.getBytes());
			out.flush();

			//receive response
			//note: a better approach would be to first read headers, determine content length
			//then read the remaining bytes as a byte stream
			String line = reader.readLine();
			while(line != null) {				
				buffer.append(line + "\n"); //append the newline stripped by readline
				line = reader.readLine();
			}

		} catch (IOException e) {
			System.out.println("HttpFetcher: download " + e.getMessage());
		}
		return buffer.toString();
	}

	private static String getRequest(String host, String path) {
		
		String request = "GET " + path + " HTTP/1.1" + "\n" 	//GET request
						+ "Host: " + host + "\n" 			//Host header required for HTTP/1.1
						+ "Connection: close\n" 				//make sure the server closes the connection after we fetch one page
						+ "\r\n";								
		return request;
	}
	

}
























