package ru.yandex.utils.jdbc;


public class MsSqlBulkInserter extends AbstractSqlBulkInserter {

    public MsSqlBulkInserter() {
    	super(500);
    }
	
    public MsSqlBulkInserter(int chunkSize) {
        super(chunkSize);
    }
    
    protected String generateSql(int elementSize, int chunkSize) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < chunkSize; i++) {
            builder.append("select ");
            for (int j = 0; j < elementSize; j++) {
                builder.append(" ? ");
                if (j != elementSize - 1) {
                    builder.append(",");
                }
            }
            if (i != chunkSize - 1) {
                builder.append(" union all ");
            }
        }
        return builder.toString();
    }

}
