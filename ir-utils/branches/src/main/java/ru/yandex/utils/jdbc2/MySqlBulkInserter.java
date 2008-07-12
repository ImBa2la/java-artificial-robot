package ru.yandex.utils.jdbc2;

import static org.apache.commons.lang.StringUtils.join;
import static ru.yandex.utils.IteratorUtils.repeat;
import static ru.yandex.utils.jdbc2.JdbcUtils.silentClose;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import ru.yandex.utils.CloseableConsumer;


/**
 * Class that inserts rows by chunks. Use multi-values INSERT sql.
 *
 * @author Dmitry Semyonov <deemonster@yandex-team.ru>
 */
public class MySqlBulkInserter<T> implements CloseableConsumer<T> {
	private static final Logger LOG = Logger.getLogger(MySqlBulkInserter.class);

	private final Object2PreparedStatementSetter<T> pstmtSetter;
	private final DataSource ds;

	public final String tableName;
	public final String fields[];

	private Object buf[];
	private int curIndex;

	public MySqlBulkInserter(
	    final DataSource dataSource,
	    final String tableName, final String fields[],
	    final int bufferSize,
	    final Object2PreparedStatementSetter<T> pstmtSetter)
	{
		this.ds = dataSource;
		this.tableName = tableName;
		this.fields = fields;
		this.pstmtSetter = pstmtSetter;

		buf = new Object[bufferSize];
		curIndex = 0;
	}

	public MySqlBulkInserter(
	    final JdbcTemplate outT,
	    final String tableName, final String fields[],
	    final int bufferSize,
	    final Object2PreparedStatementSetter<T> pstmtSetter)
	{
		this(outT.getDataSource(), tableName, fields, bufferSize, pstmtSetter);
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

	@SuppressWarnings("unchecked")
	private void outBuffer() {
		Connection cn = null;
		PreparedStatement pstmt = null;
		try {
			cn = ds.getConnection();
			pstmt = cn.prepareStatement(generateInsertSql());
			PstmtSetterProxy setProxy = new PstmtSetterProxy(pstmt);

			for (int i = 0; i < curIndex; i++)
				setProxy.setValues(i*fields.length, pstmtSetter, (T) buf[i]);

			final int res = pstmt.executeUpdate();
			if (res != curIndex)
				LOG.warn("outBuffer(): should insert " + curIndex + " rows, but inserted " + res);

			Arrays.fill(buf, null);
			curIndex = 0;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			silentClose(pstmt);
			silentClose(cn);
		}
	}

	private String generateInsertSql() {
		return
		  "insert into " + tableName +
		  " (" + join(fields, ",") + ") values " +
		  join(repeat("(" + join(repeat("?", fields.length), ",") + ")", curIndex), ", ");
	}
}
