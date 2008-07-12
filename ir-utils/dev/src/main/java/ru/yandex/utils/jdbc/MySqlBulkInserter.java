package ru.yandex.utils.jdbc;


public class MySqlBulkInserter extends AbstractSqlBulkInserter {
	
    public MySqlBulkInserter() {
    	super(500);
    }
    
    public MySqlBulkInserter(int chunkSize) {
    	super(chunkSize);
    }    

    protected String generateSql(int elementSize, int chunkSize) {
        StringBuilder builder = new StringBuilder(" values ");
        for (int i = 0; i < chunkSize; i++) {
            builder.append("(");
            for (int j = 0; j < elementSize; j++) {
                builder.append(" ? ");
                if (j != elementSize - 1) {
                    builder.append(",");
                }
            }
            if (i != chunkSize - 1) {
                builder.append("), ");
            }
        }
        builder.append(")");
        return builder.toString();
    }
}
