package ru.yandex.utils.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface BulkInserter {
    <T>void insert(String sql, List<T> elements, int elementSize, InsertRowMapper<T> rowMapper, Connection connection) throws SQLException;
}
