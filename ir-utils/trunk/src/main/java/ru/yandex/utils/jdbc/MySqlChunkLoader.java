package ru.yandex.utils.jdbc;

/**
 * Класс умеющий загружать большую таблицу частями
 *
 * @author Egor Azanov, <a href="mailto:krondix@yandex-team.ru">
 */

public class MySqlChunkLoader<T> extends AbstractChunkLoader<T> {

    protected String prepareQuery() {
        StringBuilder query = new StringBuilder();
        query.append("select ").append(sql).append(" from ").append(tableName);
    	query.append(" where ").append(where);
        if (union != null) {
            query.append(" union ").append(union).append(" ");
        }
        if(orderBy != null) {
            query.append(" order by ").append(orderBy);
    	}
        query.append(" limit ").append(chunkNumber * chunkSize).append(", ").append(chunkSize);    	
        return query.toString();
    }

}
