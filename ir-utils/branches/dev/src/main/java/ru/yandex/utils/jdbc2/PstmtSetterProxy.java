package ru.yandex.utils.jdbc2;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;



public class PstmtSetterProxy implements PreparedStatement {
	private final PreparedStatement pstmt;
	private int offset;

	public PstmtSetterProxy(final PreparedStatement pstmt) {
		this.pstmt = pstmt;
	}

	public <T> void setValues(
	    final int offset,
	    final Object2PreparedStatementSetter<T> pstmtSetter,
	    final T o)
	  throws SQLException
	{
		this.offset = offset;
		pstmtSetter.setValues(o, this);
	}

	public void addBatch() throws SQLException {
		pstmt.addBatch();
	}

	public void addBatch(final String sql) throws SQLException {
		pstmt.addBatch(sql);
	}

	public void clearParameters() throws SQLException {
		pstmt.clearParameters();
	}

	public final boolean execute() throws SQLException {
		return pstmt.execute();
	}

	public ResultSet executeQuery() throws SQLException {
		return pstmt.executeQuery();
	}

	public int executeUpdate() throws SQLException {
		return pstmt.executeUpdate();
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		return pstmt.getMetaData();
	}

	public ParameterMetaData getParameterMetaData() throws SQLException {
		return pstmt.getParameterMetaData();
	}

	public void setArray(final int i, Array v) throws SQLException {
		pstmt.setArray(offset + i, v);
	}

	public void setAsciiStream(final int i, InputStream v, final int length) throws SQLException {
		pstmt.setAsciiStream(offset + i, v, length);
	}

	public void setBigDecimal(final int i, BigDecimal v) throws SQLException {
		pstmt.setBigDecimal(offset + i, v);
	}

	public void setBinaryStream(final int i, InputStream v, final int length) throws SQLException {
		pstmt.setBinaryStream(offset + i, v, length);
	}

	public void setBlob(final int i, Blob v) throws SQLException {
		pstmt.setBlob(offset + i, v);
	}

	public void setBoolean(final int i, final boolean v) throws SQLException {
		pstmt.setBoolean(offset + i, v);
	}

	public void setByte(final int i, byte v) throws SQLException {
		pstmt.setByte(offset + i, v);
	}

	public void setBytes(final int i, byte[] v) throws SQLException {
		pstmt.setBytes(offset + i, v);
	}

	public void setCharacterStream(final int i, Reader v, final int length) throws SQLException {
		pstmt.setCharacterStream(offset + i, v, length);
	}

	public void setClob(final int i, Clob v) throws SQLException {
		pstmt.setClob(offset + i, v);
	}

	public void setDate(final int i, Date v) throws SQLException {
		pstmt.setDate(offset + i, v);
	}

	public void setDate(final int i, final Date v, Calendar cal) throws SQLException {
		pstmt.setDate(offset + i, v, cal);
	}

	public void setDouble(final int i, double v) throws SQLException {
		pstmt.setDouble(offset + i, v);
	}

	public void setFloat(final int i, float v) throws SQLException {
		pstmt.setFloat(offset + i, v);
	}

	public void setInt(final int i, final int v) throws SQLException {
		pstmt.setInt(offset + i, v);
	}

	public void setLong(final int i, long v) throws SQLException {
		pstmt.setLong(offset + i, v);
	}

	public void setNull(final int i, final int sqlType) throws SQLException {
		pstmt.setNull(offset + i, sqlType);
	}

	public void setNull(final int i, final int sqlType, final String typeName) throws SQLException {
		pstmt.setNull(offset + i, sqlType);
	}

	public void setObject(final int i, Object v) throws SQLException {
		pstmt.setObject(offset + i, v);
	}

	public void setObject(final int i, Object v, final int targetSqlType) throws SQLException {
		pstmt.setObject(offset + i, v, targetSqlType);
	}

	public void setObject(final int i, Object v, final int targetSqlType, final int scale) throws SQLException {
		pstmt.setObject(offset + i, v, targetSqlType, scale);
	}

	public void setRef(final int i, Ref v) throws SQLException {
		pstmt.setRef(offset + i, v);
	}

	public void setShort(final int i, short v) throws SQLException {
		pstmt.setShort(offset + i, v);
	}

	public void setString(final int i, final String v) throws SQLException {
		pstmt.setString(offset + i, v);
	}

	public void setTime(final int i, Time v) throws SQLException {
		pstmt.setTime(offset + i, v);
	}

	public void setTime(final int i, Time v, Calendar cal) throws SQLException {
		pstmt.setTime(offset + i, v, cal);
	}

	public void setTimestamp(final int i, Timestamp v) throws SQLException {
		pstmt.setTimestamp(offset + i, v);
	}

	public void setTimestamp(final int i, Timestamp v, Calendar cal) throws SQLException {
		pstmt.setTimestamp(offset + i, v, cal);
	}

	public void setURL(final int i, URL v) throws SQLException {
		pstmt.setURL(offset + i, v);
	}

	public void setUnicodeStream(final int i, InputStream v, final int length) throws SQLException {
		pstmt.setUnicodeStream(offset + i, v, length);
	}

	public void cancel() throws SQLException {
		pstmt.cancel();
	}

	public void clearBatch() throws SQLException {
		pstmt.clearBatch();
	}

	public void clearWarnings() throws SQLException {
		pstmt.clearWarnings();
	}

	public void close() throws SQLException {
		pstmt.close();
	}

	public final boolean execute(final String sql) throws SQLException {
		return pstmt.execute(sql);
	}

	public final boolean execute(final String sql, final int autoGeneratedKeys) throws SQLException {
		return pstmt.execute(sql, autoGeneratedKeys);
	}

	public final boolean execute(final String sql, int[] columnIndexes) throws SQLException {
		return pstmt.execute(sql, columnIndexes);
	}

	public final boolean execute(final String sql, String[] columnNames) throws SQLException {
		return pstmt.execute(sql, columnNames);
	}

	public int[] executeBatch() throws SQLException {
		return pstmt.executeBatch();
	}

	public ResultSet executeQuery(final String sql) throws SQLException {
		return pstmt.executeQuery(sql);
	}

	public final int executeUpdate(final String sql) throws SQLException {
		return pstmt.executeUpdate(sql);
	}

	public final int executeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException {
		return pstmt.executeUpdate(sql, autoGeneratedKeys);
	}

	public final int executeUpdate(final String sql, int[] columnIndexes) throws SQLException {
		return pstmt.executeUpdate(sql, columnIndexes);
	}

	public final int executeUpdate(final String sql, String[] columnNames) throws SQLException {
		return pstmt.executeUpdate(sql, columnNames);
	}

	public Connection getConnection() throws SQLException {
		return pstmt.getConnection();
	}

	public final int getFetchDirection() throws SQLException {
		return pstmt.getFetchDirection();
	}

	public final int getFetchSize() throws SQLException {
		return pstmt.getFetchSize();
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		return pstmt.getGeneratedKeys();
	}

	public final int getMaxFieldSize() throws SQLException {
		return pstmt.getMaxFieldSize();
	}

	public final int getMaxRows() throws SQLException {
		return pstmt.getMaxRows();
	}

	public final boolean getMoreResults() throws SQLException {
		return pstmt.getMoreResults();
	}

	public final boolean getMoreResults(final int current) throws SQLException {
		return pstmt.getMoreResults(current);
	}

	public final int getQueryTimeout() throws SQLException {
		return pstmt.getQueryTimeout();
	}

	public ResultSet getResultSet() throws SQLException {
		return pstmt.getResultSet();
	}

	public final int getResultSetConcurrency() throws SQLException {
		return pstmt.getResultSetConcurrency();
	}

	public final int getResultSetHoldability() throws SQLException {
		return pstmt.getResultSetHoldability();
	}

	public final int getResultSetType() throws SQLException {
		return pstmt.getResultSetType();
	}

	public final int getUpdateCount() throws SQLException {
		return pstmt.getUpdateCount();
	}

	public SQLWarning getWarnings() throws SQLException {
		return pstmt.getWarnings();
	}

	public void setCursorName(final String name) throws SQLException {
		pstmt.setCursorName(name);
	}

	public void setEscapeProcessing(final boolean enable) throws SQLException {
		pstmt.setEscapeProcessing(enable);
	}

	public void setFetchDirection(final int direction) throws SQLException {
		pstmt.setFetchDirection(direction);
	}

	public void setFetchSize(final int rows) throws SQLException {
		pstmt.setFetchSize(rows);
	}

	public void setMaxFieldSize(final int max) throws SQLException {
		pstmt.setMaxFieldSize(max);
	}

	public void setMaxRows(final int max) throws SQLException {
		pstmt.setMaxRows(max);
	}

	public void setQueryTimeout(final int seconds) throws SQLException {
		pstmt.setQueryTimeout(seconds);
	}

	public boolean isClosed() throws SQLException {
		return pstmt.isClosed();
	}

	public boolean isPoolable() throws SQLException {
		return pstmt.isPoolable();
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return pstmt.isWrapperFor(iface);
	}

	public void setAsciiStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		pstmt.setAsciiStream(parameterIndex, x, length);
	}

	public void setAsciiStream(int parameterIndex, InputStream x)
			throws SQLException {
		pstmt.setAsciiStream(parameterIndex, x);
	}

	public void setBinaryStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		pstmt.setBinaryStream(parameterIndex, x, length);
	}

	public void setBinaryStream(int parameterIndex, InputStream x)
			throws SQLException {
		pstmt.setBinaryStream(parameterIndex, x);
	}

	public void setBlob(int parameterIndex, InputStream inputStream, long length)
			throws SQLException {
		pstmt.setBlob(parameterIndex, inputStream, length);
	}

	public void setBlob(int parameterIndex, InputStream inputStream)
			throws SQLException {
		pstmt.setBlob(parameterIndex, inputStream);
	}

	public void setCharacterStream(int parameterIndex, Reader reader,
			long length) throws SQLException {
		pstmt.setCharacterStream(parameterIndex, reader, length);
	}

	public void setCharacterStream(int parameterIndex, Reader reader)
			throws SQLException {
		pstmt.setCharacterStream(parameterIndex, reader);
	}

	public void setClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		pstmt.setClob(parameterIndex, reader, length);
	}

	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		pstmt.setClob(parameterIndex, reader);
	}

	public void setNCharacterStream(int parameterIndex, Reader value,
			long length) throws SQLException {
		pstmt.setNCharacterStream(parameterIndex, value, length);
	}

	public void setNCharacterStream(int parameterIndex, Reader value)
			throws SQLException {
		pstmt.setNCharacterStream(parameterIndex, value);
	}

	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		pstmt.setNClob(parameterIndex, value);
	}

	public void setNClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		pstmt.setNClob(parameterIndex, reader, length);
	}

	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		pstmt.setNClob(parameterIndex, reader);
	}

	public void setNString(int parameterIndex, String value)
			throws SQLException {
		pstmt.setNString(parameterIndex, value);
	}

	public void setPoolable(boolean poolable) throws SQLException {
		pstmt.setPoolable(poolable);
	}

	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		pstmt.setRowId(parameterIndex, x);
	}

	public void setSQLXML(int parameterIndex, SQLXML xmlObject)
			throws SQLException {
		pstmt.setSQLXML(parameterIndex, xmlObject);
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return pstmt.unwrap(iface);
	}
	
	

	
}
