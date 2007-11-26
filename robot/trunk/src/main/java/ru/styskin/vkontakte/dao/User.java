package ru.styskin.vkontakte.dao;

import java.util.List;

public class User {
	
	private int id;
	private String name;
	private List<Integer> lazyFriends;
	private List<User> friends;

	public User(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public String generateUrl() {
		return "http://vkontakte.ru/profile.php?id=" + id;		
	}
	
	@Override
	public String toString() {
		return "user: " + id;
	}
	
}
