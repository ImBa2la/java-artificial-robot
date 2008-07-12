package ru.yandex.utils.jdbc2;

import static org.apache.commons.lang.StringUtils.join;
import static ru.yandex.utils.IteratorUtils.repeat;
import static ru.yandex.utils.jdbc2.JdbcUtils.silentClose;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;

import ru.yandex.utils.CloseableConsumer;
import ru.yandex.utils.CloseableIterator;


/**
 * Class which encapsulate DB-specific class instantiation (BulkInserter/ChunkLoader).
 *
 * @author Dmitry Semyonov <deemonster@yandex-team.ru>
 */
public class DbDialect {
	private final DataSource ds;
	private final DbType dbType;

	private int defaultLoadBufferSize = 10000;
	private int defaultOutBufferSize  = 1000;

	public DbDialect(DataSource dataSource) {
		this.ds = dataSource;
		this.dbType = detectDbType();
	}

	public <T> CloseableIterator<T> createChunkLoader(
	    final String sql, final RowMapper mapper)
	{
		final CloseableIterator<T> res;
		switch (dbType) {
		case MySql:
			res = new MySqlChunkLoader<T>(ds, sql, defaultLoadBufferSize, mapper);
			break;
		default:
			res = new GenericChunkLoader<T>(ds, sql, defaultLoadBufferSize, mapper);
			break;
		}
		return res;
	}

	public <T> CloseableConsumer<T> createBulkInserter(
	    final String tableName, final String fields[],
	    final Object2PreparedStatementSetter<T> pstmtSetter)
	{
		final CloseableConsumer<T> res;
		switch (dbType) {
		case MySql:
			res = new MySqlBulkInserter<T>(ds, tableName, fields, defaultOutBufferSize, pstmtSetter);
			break;
		default:
			res = new OracleBulkInserter<T>(ds, tableName, fields, defaultOutBufferSize, pstmtSetter);
			break;
		}
		return res;
	}

	public void createTable(
	    final String tableName,
	    final String fields[], final int fieldTypes[])
	{
		assert fields.length != fieldTypes.length : "The lengths of fields[] and fieldClasses[] must be equal";
		final String sql;
		switch (dbType) {
		case MySql:
			sql = generateMySqlCreateTable(tableName, fields, fieldTypes);
			break;
		default:
			sql = generateOracleCreateTable(tableName, fields, fieldTypes);
			break;
		}

		Connection cn = null;
		PreparedStatement pstmt = null;
		try {
			cn = ds.getConnection();
			pstmt = cn.prepareStatement(sql);
			pstmt.execute();

		} catch (SQLException e) {
			throw new RuntimeException(e);

		} finally {
			silentClose(pstmt);
			silentClose(cn);
		}
	}

	public static String generateSql(final String tableName, final String[] fields) {
		return
		  "insert into " + tableName +
		  " (" + join(fields, ",") + ") values" +
		  " (" + join(repeat("?", fields.length), ",") + ")";
	}

	private static final Int2ObjectMap<String> MYSQL_DBTYPE_NAMES = new Int2ObjectOpenHashMap<String>();
	{
		Int2ObjectMap<String> m = MYSQL_DBTYPE_NAMES;
		m.put(Types.BOOLEAN, "BIT");
		m.put(Types.BIT, "BIT");
		m.put(Types.DATE, "DATETIME");
		m.put(Types.TIME, "TIME");
		m.put(Types.TIMESTAMP, "TIMESTAMP");
		m.put(Types.VARCHAR, "VARCHAR(255)");
		m.put(Types.INTEGER, "INTEGER");
		m.put(Types.LONGVARCHAR, "TEXT");
		m.put(Types.DOUBLE, "DOUBLE");
		m.put(Types.FLOAT, "FLOAT");
	}

	private String generateMySqlCreateTable(
	    final String tableName,
	    final String fields[],
	    final int fieldTypes[])
	{
		return "create table " + tableName + "(" +
		  generateFiledList(fields, fieldTypes, MYSQL_DBTYPE_NAMES) + ") DEFAULT CHARACTER SET CP1251";
	}

	private static final Int2ObjectMap<String> ORACLE_DBTYPE_NAMES = new Int2ObjectOpenHashMap<String>();
	{
		Int2ObjectMap<String> m = ORACLE_DBTYPE_NAMES;
		m.put(Types.BOOLEAN, "NUMBER(1)");
		m.put(Types.BIT, "NUMBER(1)");
		m.put(Types.DATE, "DATE");
		m.put(Types.TIME, "DATE");
		m.put(Types.TIMESTAMP, "TIMESTAMP");
		m.put(Types.VARCHAR, "VARCHAR(255)");
		m.put(Types.INTEGER, "NUMBER(20)");
		m.put(Types.LONGVARCHAR, "VARCHER(4000)");
		m.put(Types.DOUBLE, "FLOAT");
		m.put(Types.FLOAT, "FLOAT");
	}

	private String generateOracleCreateTable(
	    final String tableName,
	    final String fields[],
	    final int fieldTypes[])
	{
		return "create table " + tableName + "(" +
		  generateFiledList(fields, fieldTypes, ORACLE_DBTYPE_NAMES) + ")";
	}

	private String generateFiledList(
	    String[] fields,
	    int[] fieldTypes,
	    Int2ObjectMap<String> typeNames)
	{
		StringBuilder b = new StringBuilder();
		if (0 < fields.length)
			b.append(fields[0]).append(" ").append(typeNames.get(fieldTypes[0]));
		for (int i = 1; i < fields.length; i++)
			b.append(",").append(fields[i]).append(" ").append(typeNames.get(fieldTypes[i]));
		return b.toString();
	}

	private DbType detectDbType() {
		DbType res;
		try {
			Connection cn = ds.getConnection();
			DatabaseMetaData md = cn.getMetaData();
			final String dbName = md.getDatabaseProductName().toLowerCase();
			if (-1 != dbName.indexOf("mysql")) {
				res = DbType.MySql;
			} else if (-1 != dbName.indexOf("oracle")) {
				res = DbType.Oracle;
			} else {
				res = DbType.Other;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return res;
	}

	private enum DbType {
		MySql, Oracle, Other
	};
}
