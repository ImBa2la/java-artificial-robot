package ru.yandex.utils.jdbc;

import java.util.Iterator;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * TODO javadoc
 *
 * @author Egor Azanov, <a href="mailto:krondix@yandex-team.ru">
 */
public class ResultSetIterator<T> implements Iterator<T> {
    private ResultSet resultSet;
    private org.springframework.jdbc.core.RowMapper rowMapper;
    private CloseCallback closeCallback;

    private boolean hasNext = true;
    private int i = 0;
    private int size;

    public int size() {
        return size;
    }

    public static interface CloseCallback {
        void close();
    }

    public ResultSetIterator(ResultSet resultSet, org.springframework.jdbc.core.RowMapper rowMapper, CloseCallback closeCallback, int size) {
        this.resultSet = resultSet;
        this.rowMapper = rowMapper;
        this.closeCallback = closeCallback;
        this.size = size;
    }

    public boolean hasNext() {
        return hasNext;
    }

    @SuppressWarnings("unchecked")
    public T next() {
        if (hasNext) {
            try {
                if (resultSet.next()) {
                    return (T)rowMapper.mapRow(resultSet, ++i);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        hasNext = false;
        closeCallback.close();
        return null;
    }

    public void remove() {
        throw new UnsupportedOperationException("Method not implemented");
    }
}
