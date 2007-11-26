package ru.styskin.vkontakte;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import ru.styskin.vkontakte.dao.User;

public class Robot implements Runnable {
	
	private static final Logger logger = Logger.getLogger(Robot.class); 
	
	private Map<Integer, User> users = new HashMap<Integer, User>();
	
	private FriendsChecker friendsChecker;
	private OnlineChecker onlineChecker;
	private int ownId;
	
	User addUser(int id) {
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

	public void setOnlineChecker(OnlineChecker onlineChecker) {
		this.onlineChecker = onlineChecker;
	}

	public void setOwnId(int ownId) {
		this.ownId = ownId;
	}
	
	public void run() {
		User me = addUser(ownId);				
		friendsChecker.workOn(me);
		for(User u : users.values()) {
			onlineChecker.workOn(u);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				logger.error("Sleep interrupted", e);
			}
		}		
	}	
	
}
