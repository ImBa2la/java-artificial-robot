package ru.yandex.utils.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractSqlBulkInserter implements BulkInserter {
	
    protected int chunkSize = 20;

    public AbstractSqlBulkInserter() {
    }

    public AbstractSqlBulkInserter(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public <T> void insert(String sql, List<T> elements, int elementSize, InsertRowMapper<T> rowMapper, Connection connection) throws SQLException {
        String chunkSql = sql + " " + generateSql(elementSize, chunkSize);

        connection.setAutoCommit(false);
        PreparedStatement statement = connection.prepareStatement(chunkSql);
        int i = 0;
        int count = 0;
        for (T element : elements) {
            rowMapper.mapRow(i * elementSize, element, statement);
            i++;
            count++;
            if (i == chunkSize) {
                statement.executeUpdate();
                i = 0;
            }
        }
        statement.close();

        if (i != 0) {
            statement = connection.prepareStatement(sql + " " + generateSql(elementSize, i));
            for (int j = count - i; j < elements.size(); j++) {
                rowMapper.mapRow((j - count + i) * elementSize, elements.get(j), statement);
            }
            statement.executeUpdate();
            statement.close();
        }

        connection.commit();
    }

    protected abstract String generateSql(int elementSize, int chunkSize);

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

}
