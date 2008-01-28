package ru.styskin.vkontakte.dao;

import java.util.HashMap;
import java.util.Map;

import javax.print.attribute.HashAttributeSet;


public class User {
	
	private int id;
	private boolean online;
	private String name;
	private String picture;
	private Map<String, String> interests = new HashMap<String, String>();
	
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
	
	public void addInterest(String f, String v) {
		interests.put(f, v);		
		updated = true;
	}
	
	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		if(this.picture == null || !this.picture.equals(picture)) {
			this.picture = picture;
			updated = true;
		}
	}

	public Map<String, String> getInterests() {
		return interests;
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
