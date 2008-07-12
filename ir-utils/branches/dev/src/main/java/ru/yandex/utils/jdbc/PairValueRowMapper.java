package ru.yandex.utils.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ru.yandex.utils.Pair;

public class PairValueRowMapper<F, S> implements InsertRowMapper<Pair<F, S>> {

	public void mapRow(int index, Pair<F, S> element, PreparedStatement statement) throws SQLException {
		statement.setObject(index + 1, element.getFirst());
		statement.setObject(index + 2, element.getSecond());
	}

	public int getSize() {
		return 2;
	}

}
