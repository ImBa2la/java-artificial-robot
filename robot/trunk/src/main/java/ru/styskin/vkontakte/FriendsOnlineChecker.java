package ru.styskin.vkontakte;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import ru.styskin.vkontakte.dao.ConnectionDao;
import ru.styskin.vkontakte.dao.User;

public class FriendsOnlineChecker implements Worker {
	
	private static final Logger logger = Logger.getLogger(FriendsOnlineChecker.class);
	
	private ConnectionDao connectionDao;

	public void workOn(User user, UserContainer container) {
		try {
			container.setAll(new Reducer<User>() {
				public void workOn(User u) {
					u.setOnline(false);
				}
			});
			
			Pattern pattern = Pattern.compile(".*<a href=\"profile\\.php\\?id=(\\d+)\">(.+)</a>.*");
			URLConnection connection = connectionDao.createConnection("http://vkontakte.ru/friend.php?act=online&id=" + user.getId());
		    BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				Matcher matcher = pattern.matcher(line);
				if(matcher.matches()) {
					logger.info(matcher.group(1));
					int userId = Integer.parseInt(matcher.group(1));
					User u = container.getUser(userId);
					u.setOnline(true);				
				}
			}
			logger.warn(user);
			rd.close();
		} catch (Throwable e) {
			logger.error("Cannot load friends list", e);
		}
	}

	public void setConnectionDao(ConnectionDao connectionDao) {
		this.connectionDao = connectionDao;
	}
	
	
		
}
