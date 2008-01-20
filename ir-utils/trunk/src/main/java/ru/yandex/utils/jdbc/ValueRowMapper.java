package ru.yandex.utils.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ValueRowMapper<T> implements InsertRowMapper<T> {

	public void mapRow(int index, T element, PreparedStatement statement) throws SQLException {
		statement.setObject(index + 1, element);
	}

	public int getSize() {
		return 1;
	}

}
