package ru.yandex.utils.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class OracleChunkLoader<T>  extends JdbcDaoSupport implements ChunkLoader<T> {
	
	private static final Logger logger = Logger.getLogger(OracleChunkLoader.class);
	
	private String tableName;
	private String sql;
	private String where = " 1=1 ";
	private String order;
	// TODO: union
	private String union;
	
	private RowMapper rowMapper;
	private int chunkSize;
	
	private class OracleConnectionCallback implements ConnectionCallback, Runnable {
		
		private boolean stopFlag = true;
		private Semaphore lock = new Semaphore(0);
		private boolean hasNextFlag = true;
		
		private boolean task = false;
		private Object monitor = new Object();
		private Collection<T> collection; 
		
		public OracleConnectionCallback() {
			Thread thread = new Thread(this);
			thread.start();
		}
		
		@SuppressWarnings("unchecked")
		public Object doInConnection(Connection connection) throws SQLException, DataAccessException {
			Statement st = connection.createStatement();
			st.setFetchSize(chunkSize);
			ResultSet rs = st.executeQuery(generateSql());
			try {
				while(stopFlag) {
					if(task) {
						synchronized (monitor) {
							int n = 0;
							collection = new LinkedList<T>();
							while(hasNextFlag && n < chunkSize) {
								hasNextFlag = rs.next();
								if(!hasNextFlag) {
									break;
								}
								collection.add((T) rowMapper.mapRow(rs, 0));								
								n ++;
							}
							monitor.notify();
							if(!hasNextFlag) {
								break;
							}
						}
					}					
					lock.acquire();
				}			
			} catch (InterruptedException e) {
				logger.error("", e);
			}
			return null;
		}
		
		public Collection<T> nextChunk() {
			task = true;
			lock.release();
			try {
				monitor.wait();
			} catch (InterruptedException e) {
				logger.error("", e);
			}
			return collection;
		}
		
		public boolean hasNext() {
			return hasNextFlag;
		}
		
		public void stop() {
			lock.release();
			stopFlag = false;
		}

		public void run() {			
			getJdbcTemplate().execute(this);			
		}		
	}
	
	
	private OracleConnectionCallback connectionCallback;

	protected void init() {
		tableName = null;
		sql = null;
		where = null;
		order = null;
		union = null;
		if(connectionCallback != null) {
			connectionCallback.stop();
			connectionCallback = null;
		}
	}

	public void init(String sql, int chunkSize, RowMapper rowMapper, DataSource dataSource, String tableName) {
		init();
		setDataSource(dataSource);
		this.sql = sql;
		this.chunkSize = chunkSize;
		this.rowMapper = rowMapper;
		this.tableName = tableName;		
	}

	public void init(String sql, String where, int chunkSize, RowMapper rowMapper, DataSource dataSource, String tableName) {
		init(sql, chunkSize, rowMapper, dataSource, tableName);
		this.where = where;
	}

	public void init(String sql, String where, String order, int chunkSize,	RowMapper rowMapper, DataSource dataSource, String tableName) {
		init(sql, where, chunkSize, rowMapper, dataSource, tableName);
		this.order = order;
	}

	public void init(String sql, String where, String order, String union, int chunkSize, RowMapper rowMapper, DataSource dataSource, String tableName) {
		init(sql, where, order, chunkSize, rowMapper, dataSource, tableName);
		this.union = union;		
	}
	
	private void createQuery() {
		if(connectionCallback == null) {
			connectionCallback = new OracleConnectionCallback();
		}		
	}

	public boolean hasNext() {
		createQuery();
		return connectionCallback.hasNext();
	}
	
	public Collection<T> nextChunk() {
		createQuery();
		return connectionCallback.nextChunk();
	}
	
	protected String generateSql() {
		StringBuilder query = new StringBuilder();
        query.append("select ").append(sql).append(" from ").append(tableName);
    	query.append(" where ").append(where);
/* FIXME  
		if (union != null) {
            query.append(" union ").append(union).append(" ");
        }*/        
    	if(order != null) {
            query.append(" order by ").append(order);
    	}
    	return query.toString();
	}

}
