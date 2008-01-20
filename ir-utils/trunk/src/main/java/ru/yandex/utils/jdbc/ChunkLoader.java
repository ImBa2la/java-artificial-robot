package ru.yandex.utils.jdbc;

import java.util.Collection;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;

/**
 * @author Egor Azanov, <a href="mailto:krondix@yandex-team.ru">
 */
public interface ChunkLoader<T> {
    void init(String sql, int chunkSize, RowMapper rowMapper, DataSource dataSource, String tableName);

    void init(String sql, String where, int chunkSize, RowMapper rowMapper, DataSource dataSource, String tableName);

    void init(String sql, String where, String order, int chunkSize, RowMapper rowMapper, DataSource dataSource, String tableName);

    void init(String sql, String where, String order, String union, int chunkSize, RowMapper rowMapper, DataSource dataSource, String tableName);
    
    Collection<T> nextChunk();
    
    boolean hasNext();
}
