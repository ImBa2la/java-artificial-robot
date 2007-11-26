package ru.styskin.utils.proxy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;


public class Proxy implements Runnable {
	
	private Socket socket;
	
	public Proxy(Socket socket) {
		this.socket = socket;		
	}
	

	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
			
			// GET http://www.w3.org/pub/WWW/TheProject.html HTTP/1.1
			String first = in.readLine();
			StringTokenizer st = new StringTokenizer(first);
			st.nextToken();			
			URL url = new URL(st.nextToken());			
			URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			BufferedOutputStream serverIn = new BufferedOutputStream(connection.getOutputStream());
			BufferedInputStream serverOut = new BufferedInputStream(connection.getInputStream());
			serverIn.write((first + "\r\n").getBytes());
			int c;
			System.out.println(first);
			while((c = in.read()) >= 0) {
				serverIn.write(c);
				System.out.write(c);
			}
			while((c = serverOut.read()) >= 0) {
				out.write(c);				
				System.out.write(c);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	
}
