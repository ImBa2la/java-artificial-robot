package ru.styskin.vkontakte;

import ru.styskin.vkontakte.dao.User;

public interface UserContainer {
	
	User getUser(int id);
	
	void setAll(Reducer<User> reduser);

}
