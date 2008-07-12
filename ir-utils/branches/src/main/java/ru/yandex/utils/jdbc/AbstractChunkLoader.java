package ru.yandex.utils.jdbc;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.sql.DataSource;
import java.util.Collection;

/**
 * @author Egor Azanov, <a href="mailto:krondix@yandex-team.ru">
 */
public abstract class AbstractChunkLoader<T> extends JdbcDaoSupport implements ChunkLoader<T> {

	protected String sql;
    protected String where = "1=1";
    protected String orderBy;
    protected int chunkSize;
    protected int chunkNumber = 0;
    protected RowMapper rowMapper;
    protected boolean hasNext = true;
    protected String tableName;
    private MySqlChunkLoader<T> nextLoader;
    protected String union;


    public void init(String sql, int chunkSize, RowMapper rowMapper, DataSource dataSource, String tableName) {
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
    
    public void init(String sql, String where, String orderBy, int chunkSize, RowMapper rowMapper, DataSource dataSource, String tableName) {
        init(sql, where, chunkSize, rowMapper, dataSource, tableName);
        this.orderBy = orderBy;
    }

    public void init(String sql, String where, String order, String union, int chunkSize, RowMapper rowMapper, DataSource dataSource, String tableName) {
        init(sql, where, order, chunkSize, rowMapper, dataSource, tableName);
        this.union = union;
    }

    @SuppressWarnings("unchecked")
    public Collection<T> nextChunk() {
        if (!hasNext) {
            return nextLoader.nextChunk();
        }

        Collection<T> result = getJdbcTemplate().query(prepareQuery(), rowMapper);
        chunkNumber++;
        hasNext = result.size() == chunkSize;
        return result;
    }
    
    @SuppressWarnings("unchecked")
    public Collection<T> getChunk(int number) {
    	int temp = chunkNumber;
    	chunkNumber = number;
        Collection<T> result = getJdbcTemplate().query(prepareQuery(), rowMapper);
        chunkNumber = temp;
        return result;
    }

    protected abstract String prepareQuery();
    
    public boolean hasNext() {
        return hasNext || (nextLoader != null && nextLoader.hasNext());
    }

    public void setNextLoader(MySqlChunkLoader<T> nextLoader) {
        this.nextLoader = nextLoader;
    }
}
