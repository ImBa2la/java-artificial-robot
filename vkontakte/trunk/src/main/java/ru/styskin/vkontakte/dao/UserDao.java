package ru.styskin.vkontakte.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import ru.styskin.utils.Triple;

public class UserDao extends JdbcDaoSupport {
	
	private String sessionTime = "500";
	
	@SuppressWarnings("unchecked")
	private List<Integer> getUsers() {
		return getJdbcTemplate().queryForList("select id from vkontakte_status", Integer.class);
	}
	
	
	private void createUsers(Collection<User> users) {
		for(User u : users) {
			getJdbcTemplate().update("insert into vkontakte_status(id) values(?)", new Object[] { u.getId()});
			getJdbcTemplate().update("insert into vkontakte_info(id, name, picture) values(?, ?, ?)", new Object[] { u.getId(), u.getName(), u.getPicture()});
			getJdbcTemplate().update("insert into vkontakte_queue(uid, path) values(?, ?)", new Object[] { String.valueOf(u.getId()), u.getPicture()});
			u.setUpdated(false);
		}
	}
	
	private void updateUsers(Collection<User> users) {
		for(User u : users) {
			if(u.isUpdated()) {
				getJdbcTemplate().update("update vkontakte_info set name = ?, picture = ? where id = ?", new Object[] { u.getName(), u.getPicture(), u.getId()});
				getJdbcTemplate().update("insert into vkontakte_queue(uid, path) values(?, ?)", new Object[] { u.getId(), u.getPicture()});
				u.setUpdated(false);
			}
		}
	}	
	
	private void usersOnline(Collection<User> users) {
		getJdbcTemplate().execute("truncate table user_online");
		for(User u : users) {
			if(u.isOnline()) {
				getJdbcTemplate().update("insert into user_online(id) values(?)", new Object[] { u.getId()});
			}
		}
	}
	
	private void finishSessions() {
		getJdbcTemplate().update("insert into vkontakte_session (id, start_session, end_session)" +
				" select id, last_session_date as start_session, last_seen_date as end_session from vkontakte_status " +
				" where last_session_date is not null and addtime(last_seen_date, ?) < NOW()", new Object[] {sessionTime});

		getJdbcTemplate().update("update vkontakte_status set last_session_date = null " +
				" where last_session_date is not null and addtime(last_seen_date, ?) < NOW()", new Object[] {sessionTime});
		
		getJdbcTemplate().update("update vkontakte_status set last_seen_date = NOW() where id in (select id from user_online)");
	}
	
	private void startSessions() {
		getJdbcTemplate().update("update vkontakte_status set last_session_date = NOW(), last_seen_date = NOW() where last_session_date is null and id in (select id from user_online)");
	}	
	
	public void saveUsers(final Collection<User> users) {
		Set<Integer> exists = new HashSet<Integer>(getUsers());
		List<User> toCreate = new ArrayList<User>();
		for(User u : users) {
			if(!exists.contains(u.getId())) {
				toCreate.add(u);
			}
		}
		createUsers(toCreate);
		updateUsers(users);
		usersOnline(users);
		finishSessions();
		startSessions();
	}
	
	@SuppressWarnings("unchecked")
	public Triple<Integer, String, String> next() {
		Triple<Integer, String, String> triple = null;
		try {
			triple = (Triple<Integer, String, String>) getJdbcTemplate().queryForObject("select id, uid, path from vkontakte_queue where id = ( select min(id) from vkontakte_queue where done = 0)", new RowMapper() {
				public Object mapRow(ResultSet rs, int index) throws SQLException {				
					return Triple.makeTriple(rs.getInt(1), rs.getString(2), rs.getString(3));
				}						
			});
		} catch (Throwable e) {
			logger.debug("No in queue", e);
		}
		return triple;
	}
	
	public void setDone(int index) {
		getJdbcTemplate().update("update vkontakte_queue set done = 1 where id = ?", new Object[] {index});
	}

	public void setSessionTime(String sessionTime) {
		this.sessionTime = sessionTime;
	}	
}
