package ru.styskin.vkontakte;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import ru.styskin.vkontakte.dao.User;
import ru.styskin.vkontakte.dao.UserDao;

public class Robot extends TimerTask implements UserContainer {
	
	private static final Logger logger = Logger.getLogger(Robot.class); 
	
	private Map<Integer, User> users = new HashMap<Integer, User>();
	
	private FriendsChecker friendsChecker;
	private FriendsOnlineChecker friendsOnlineChecker;
	private UserDao userDao;
	private int ownId;
	
	public User getUser(int id) {
		User user = users.get(id);
		if(user == null) {
			user = new User(id);
			users.put(id, user);
		}
		return user;		
	}
	
	public void addUser(User user) {
		if(!users.containsKey(user.getId())) {
			users.put(user.getId(), user);
		}
	}	
		

	public void setFriendsChecker(FriendsChecker friendsChecker) {
		this.friendsChecker = friendsChecker;
	}

	public void setFriendsOnlineChecker(FriendsOnlineChecker friendsOnlineChecker) {
		this.friendsOnlineChecker = friendsOnlineChecker;
	}

	public void setOwnId(int ownId) {
		this.ownId = ownId;
	}
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void run() {
		User me = getUser(ownId);				
		friendsChecker.workOn(me, this);
		friendsOnlineChecker.workOn(me, this);
		userDao.saveUsers(users.values());		
	}

	public void setAll(Reducer<User> reduser) {
		for(User u : users.values()) {
			reduser.workOn(u);			
		}
	}	
	
}
