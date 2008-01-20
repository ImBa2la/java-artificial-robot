package ru.yandex.utils.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface InsertRowMapper<T> {
	
    void mapRow(int index, T element, PreparedStatement statement) throws SQLException;
}
