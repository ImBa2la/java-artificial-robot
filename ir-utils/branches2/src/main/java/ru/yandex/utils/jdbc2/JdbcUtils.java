package ru.yandex.utils.jdbc2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;


/**
 * Utility class for Jdbc.
 *
 * @author Dmitry Semyonov <deemonster@yandex-team.ru>
 */
public final class JdbcUtils {
	private final static Logger LOG = Logger.getLogger(JdbcUtils.class);

	private JdbcUtils() { }

	/** Close object without exception */
	public static void silentClose(Connection o) {
		try {
			if (null != o)
				o.close();
		} catch (SQLException e) {
			// PASS
		}
	}

	/** Close object without exception */
	public static void silentClose(PreparedStatement o) {
		try {
			if (null != o)
				o.close();
		} catch (SQLException e) {
			// PASS
		}
	}

	/** Close object without exception */
	public static void silentClose(ResultSet o) {
		try {
			if (null != o)
				o.close();
		} catch (SQLException e) {
			// PASS
		}
	}


	/** Execute SQL without exception */
	public static void silentExecute(DataSource ds, final String sql) {
		try {
			new JdbcTemplate(ds).execute(sql);
		} catch (DataAccessException e) {
			LOG.warn("Error during execute sql, sql=[" + sql + "], " + e.getMessage());
		}
	}
}
