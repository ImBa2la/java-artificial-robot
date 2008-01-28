package ru.styskin.vkontakte;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import ru.styskin.utils.Pair;
import ru.styskin.vkontakte.dao.ConnectionDao;
import ru.styskin.vkontakte.dao.User;

public class FriendsChecker implements Worker {
	
	private static final Logger logger = Logger.getLogger(FriendsChecker.class);
	
	private ConnectionDao connectionDao;

	public void workOn(User user, UserContainer container) {
		try {
			Pattern namePattern = Pattern.compile(".*<a .*href=\"profile\\.php\\?id=(\\d+)\">([^<>]+)</a>.*");
			Pattern picturePattern = Pattern.compile(".*<a .*href=\"profile\\.php\\?id=(\\d+)\"><IMG SRC='(.*)' ALT='' /></a>.*");
			List<Pair<Pattern, ? extends PropertySetter<User, String>>> patterns = new ArrayList<Pair<Pattern,? extends PropertySetter<User,String>>>();
			patterns.add(Pair.makePair(namePattern, new PropertySetter<User, String>() {
				public void workOn(User u, String s) {
					u.setName(s);										
				}
			}));
			patterns.add(Pair.makePair(picturePattern, new PropertySetter<User, String>() {
				public void workOn(User u, String s) {
					u.setPicture(s);
				}
			}));
			URLConnection connection = connectionDao.createConnection("http://vkontakte.ru/friend.php?id=" + user.getId());
		    BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("cp1251")));
			String line;
			while ((line = rd.readLine()) != null) {
				for(Pair<Pattern, ? extends PropertySetter<User, String>> pair : patterns) {
					Matcher matcher = pair.getFirst().matcher(line);
					if(matcher.matches()) {
						logger.info(matcher.group(2));
						int userId = Integer.parseInt(matcher.group(1));
						String value = matcher.group(2);
						User u = container.getUser(userId);
						pair.getSecond().workOn(u, value);
					}					
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
