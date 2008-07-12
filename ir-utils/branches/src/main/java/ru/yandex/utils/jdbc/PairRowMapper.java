package ru.yandex.utils.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ru.yandex.utils.Pair;

public class PairRowMapper implements RowMapper {

	@SuppressWarnings("unchecked")
	public Object mapRow(ResultSet rs, int arg1) throws SQLException {
		return Pair.makePair(rs.getObject(1), rs.getObject(2));
	}

}
