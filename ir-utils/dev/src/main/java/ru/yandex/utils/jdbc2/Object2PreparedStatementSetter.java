package ru.yandex.utils.jdbc2;

import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * Use this interface to set prepared statement properties from object.
 * @author Dmitry Semyonov <deemonster@yandex-team.ru>
 */
public interface Object2PreparedStatementSetter<T> {
	void setValues(T object, PreparedStatement pstmt) throws SQLException;
}
