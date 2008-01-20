package ru.yandex.utils.jdbc;

/**
 * @author Egor Azanov, <a href="mailto:krondix@yandex-team.ru">
 */
public class MsSqlChunkLoader<T> extends AbstractChunkLoader<T> {
	
    protected String prepareQuery() {
        StringBuilder query = new StringBuilder();
        query.append("select ").append(sql).append(" from ").append(tableName);
        query.append(" where id in (select top ").append(chunkSize).append(" id from ").append(tableName);
        if (chunkNumber == 0) {
        	if(orderBy != null) {
            	query.append(" where ").append(where).append(" order by ").append(orderBy).append(" ) ").append(" order by ").append(orderBy);
        	} else {
            	query.append(" where ").append(where).append(")");        		
        	}
        } else {
        	query.append(" where ").append(where).append(" and id not in (select top ").
            append(chunkNumber * chunkSize).
            append(" id from ").
            append(tableName).append(" where ").append(where);
        	if(orderBy != null) {
                query.append(" order by ").append(orderBy).append(") order by ").append(orderBy).append(") order by ").append(orderBy);
        	} else {
        		query.append(" ) ) ");
        	}
        }        
        return query.toString();
    }
    
}
