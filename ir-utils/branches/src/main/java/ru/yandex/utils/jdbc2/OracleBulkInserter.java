package ru.yandex.utils.jdbc2;

import static ru.yandex.utils.IntUtils.min2;
import static ru.yandex.utils.jdbc2.DbDialect.generateSql;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import ru.yandex.utils.CloseableConsumer;


/**
 * Class that inserts rows by chunks. Use batchUpdate method.
 *
 * @author Dmitry Semyonov <deemonster@yandex-team.ru>
 */
public class OracleBulkInserter<T> implements CloseableConsumer<T> {
	private final String sql;
	private final Object2PreparedStatementSetter<T> pstmtSetter;
	private final JdbcTemplate outT;

	private Object buf[];
	private int curIndex;

	public OracleBulkInserter(
	    final DataSource dataSource,
	    final String tableName, final String fields[],
	    final int bufferSize,
	    final Object2PreparedStatementSetter<T> pstmtSetter)
	{
		this(new JdbcTemplate(dataSource), tableName, fields, bufferSize, pstmtSetter);
	}

	public OracleBulkInserter(
	    final JdbcTemplate outT,
	    final String tableName, final String fields[],
	    final int bufferSize,
	    final Object2PreparedStatementSetter<T> pstmtSetter)
	{
		this.outT = outT;
		this.sql = generateSql(tableName, fields);
		this.pstmtSetter = pstmtSetter;

		buf = new Object[bufferSize];
		curIndex = 0;
	}

	public void add(T o) {
		buf[curIndex++] = o;
		if (buf.length <= curIndex)
			outBuffer();
	}

	public void close() throws IOException {
		if (0 < curIndex)
			outBuffer();
	}

	private void outBuffer() {
		outT.batchUpdate(sql, new BatchPreparedStatementSetter() {
			public int getBatchSize() {
				return min2(buf.length, curIndex);
			}

			@SuppressWarnings("unchecked")
			public void setValues(PreparedStatement pstmt, final int index) throws SQLException {
				pstmtSetter.setValues((T) buf[index], pstmt);
			}
		});

		Arrays.fill(buf, null);
		curIndex = 0;
	}
}
