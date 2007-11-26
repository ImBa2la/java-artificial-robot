package ru.styskin.vkontakte;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLConnection;

import org.apache.log4j.Logger;

import ru.styskin.vkontakte.dao.ConnectionDao;
import ru.styskin.vkontakte.dao.User;

public class OnlineChecker implements Worker {
	
	private static final Logger logger = Logger.getLogger(OnlineChecker.class);
	
	private ConnectionDao connectionDao;
	private String onlineString = "<b>Online</b>";
	
	public void workOn(User user) {
		try {
			URLConnection connection = connectionDao.createConnection(user.generateUrl());				
		    BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			boolean online = false;
			while ((line = rd.readLine()) != null) {
				if(line.indexOf(onlineString) >= 0) {
					online = true;
					break;										
				}				
			}
			logger.warn(user);
			rd.close();
		} catch (Throwable e) {
			logger.error("Cannot check  " + user);
		}		
	}
	
		

	public void setConnectionDao(ConnectionDao connectionDao) {
		this.connectionDao = connectionDao;
	}	

}
