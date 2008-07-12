package ru.yandex.utils.jdbc2;

import static ru.yandex.utils.jdbc2.JdbcUtils.silentClose;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;

import ru.yandex.utils.CloseableIterator;

public class GenericChunkLoader<T> implements CloseableIterator<T> {
	private final static Logger LOG = Logger.getLogger(GenericChunkLoader.class);

	private final JdbcTemplate inT;
	private final Connection cn;
	private final String sql;
	private final Object args[];
	private final RowMapper mapper;

	private final int bufferSize;
	private T buf;
	private ResultSet rs;

	private int row;

	public GenericChunkLoader(DataSource dataSource, final String sql, int bufferSize, RowMapper mapper) {
		this(new JdbcTemplate(dataSource), sql, bufferSize, mapper);
	}

	public GenericChunkLoader(DataSource dataSource, final String sql, Object[] args, final int bufferSize, RowMapper mapper) {
		this(new JdbcTemplate(dataSource), sql, args, bufferSize, mapper);
	}

	public GenericChunkLoader(JdbcTemplate inT, final String sql, int bufferSize, RowMapper mapper) {
		this(inT, sql, new Object[] {}, bufferSize, mapper);
	}

	public GenericChunkLoader(JdbcTemplate inT, final String sql, Object[] args, final int bufferSize, RowMapper mapper) {
		try {
			this.inT = inT;
			this.cn = inT.getDataSource().getConnection();
		} catch (SQLException e) {
			throw new RuntimeException("Could not get connection", e);
		}

		this.rs = null;
		this.bufferSize = bufferSize;
		this.sql = sql;
		this.args = args;
		this.mapper = mapper;
		this.buf = null;
	}

	public boolean hasNext() {
		if (null == rs)
			fetchBuf();
		return null != buf;
	}

	public T next() {
		T res = buf;
		fetchBuf();
		return res;
	}

	public void remove() { }

	public void close() {
		safeClose();
	}

	@SuppressWarnings("unchecked")
	private void fetchBuf() {
		try {
			if (null == rs) {
				PreparedStatement pstmt = cn.prepareStatement(sql);
				pstmt.setQueryTimeout(inT.getQueryTimeout());
				setArgs(pstmt, args);
				pstmt.setFetchSize(bufferSize);
				rs = pstmt.executeQuery();
			}

			buf = null;
			if (rs.next())
				this.buf = (T) mapper.mapRow(rs, row++);

		} catch (SQLException e) {
			System.out.println("aaa");
			safeClose();
			LOG.warn("fetchBuf(): fetch error", e);
		}
	}

	private static void setArgs(PreparedStatement pstmt, Object[] args) throws SQLException {
		for (int i = 0; i < args.length; i++) {
			if (args[i] instanceof SqlParameterValue) {
				SqlParameterValue paramValue = (SqlParameterValue) args[i];
				StatementCreatorUtils.setParameterValue(pstmt, i + 1, paramValue, paramValue.getValue());
			}
			else {
				StatementCreatorUtils.setParameterValue(pstmt, i + 1, SqlTypeValue.TYPE_UNKNOWN, args[i]);
			}
		}
	}

	private void safeClose() {
		silentClose(rs);
		silentClose(cn);
	}
}
