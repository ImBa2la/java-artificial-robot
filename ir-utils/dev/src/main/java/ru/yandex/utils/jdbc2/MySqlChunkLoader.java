package ru.yandex.utils.jdbc2;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import ru.yandex.utils.CloseableIterator;

public class MySqlChunkLoader<T> implements CloseableIterator<T> {
	private final JdbcTemplate inT;
	private final String sql;
	private final RowMapper mapper;

	private final Object buf[];
	private int fetched;
	private int currentIndex;
	private int offset;

	public MySqlChunkLoader(DataSource dataSource, final String sql, final int bufferSize, RowMapper mapper) {
		this(new JdbcTemplate(dataSource), sql, bufferSize, mapper);
	}

	public MySqlChunkLoader(JdbcTemplate inT, final String sql, final int bufferSize, RowMapper mapper) {
		this.inT = inT;
		this.sql = sql;
		this.mapper = mapper;
		this.buf = new Object[bufferSize];
		fetched = -1;
		offset = 0;
	}

	public boolean hasNext() {
		if (-1 == fetched || fetched <= currentIndex)
			fetchBuf();

		return 0 < fetched;
	}

	public T next() {
		if (-1 == fetched || fetched <= currentIndex)
			fetchBuf();

		if (0 == fetched || fetched <= currentIndex)
			return null;

		@SuppressWarnings("unchecked")
		T res = (T) buf[currentIndex++];
		return res;
	}

	public void remove() { }

	public void close() throws IOException { }

	private void fetchBuf() {
		fetched = 0;
		currentIndex = 0;
		inT.query(sql + " limit " + offset + ", " + buf.length, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				buf[fetched] = mapper.mapRow(rs, fetched);
				fetched++;
			}
		});
		offset += buf.length;
	}
}
