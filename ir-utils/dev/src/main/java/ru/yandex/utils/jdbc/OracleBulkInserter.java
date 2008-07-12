package ru.yandex.utils.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OracleBulkInserter implements BulkInserter {

    protected int chunkSize = 1000;

    public OracleBulkInserter() {
    }

    public OracleBulkInserter(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public <T> void insert(String sql, List<T> elements, int elementSize, InsertRowMapper<T> rowMapper, Connection connection) throws SQLException {
        String chunkSql = sql + " " + generateSql(elementSize);

        connection.setAutoCommit(false);
        PreparedStatement statement = connection.prepareStatement(chunkSql);
        int i = 0;
        int count = 0;
        for (T element : elements) {        	
            rowMapper.mapRow(0, element, statement);            
            i++;
            count++;
            if (i == chunkSize) {
                statement.executeBatch();
                i = 0;
            } else {
            	statement.addBatch();
            }
        }
        statement.close();
        if (i != 0) {
            statement = connection.prepareStatement(sql + " " + generateSql(elementSize));
            for (int j = count - i; j < elements.size(); j++) {
                rowMapper.mapRow(0, elements.get(j), statement);
                if(j != elements.size() -1) {
                	statement.addBatch();
                }                
            }
            statement.executeBatch();
            statement.close();
        }
        connection.commit();
    }

    protected String generateSql(int elementSize) {
        StringBuilder builder = new StringBuilder(" values ");
        builder.append("(");
        for (int j = 0; j < elementSize; j++) {
            builder.append(" ? ");
            if (j != elementSize - 1) {
                builder.append(",");
            }
        }
        builder.append(")");
        return builder.toString();
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

}
