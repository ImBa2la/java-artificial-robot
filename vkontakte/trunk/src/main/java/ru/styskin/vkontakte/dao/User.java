package ru.styskin.vkontakte.dao;

import java.util.List;

public class User {
	
	private int id;
	private boolean online;
	private String name;
	private List<Integer> lazyFriends;
	private List<User> friends;
	
	private boolean updated = false;

	public User(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public boolean isOnline() {
		return online;
	}
	
	public void setOnline(boolean online) {
		this.online = online;
	}	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if(this.name == null || !this.name.equals(name)) {
			this.name = name;
			updated = true;
		}
	}
	
	public boolean isUpdated() {
		return updated;
	}	

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	public String generateUrl() {
		return "http://vkontakte.ru/profile.php?id=" + id;		
	}
	
	@Override
	public String toString() {
		return "user: " + id;
	}
	
}
