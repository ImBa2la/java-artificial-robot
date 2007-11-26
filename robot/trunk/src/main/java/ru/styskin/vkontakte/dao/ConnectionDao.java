package ru.styskin.vkontakte.dao;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ConnectionDao {
	
	private String cookie;	
	
	public URLConnection createConnection(String url) throws MalformedURLException, IOException {
		URLConnection connection = new URL(url).openConnection();
		connection.setDoOutput(true);
		connection.setRequestProperty("Cookie", cookie);
		return connection;
	}
	
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}	

}
