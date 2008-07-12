package ru.yandex.market.dao;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import ru.yandex.market.CategoryTree;
import ru.yandex.utils.Pair;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class CategoryTreeDaoJdbc extends JdbcDaoSupport implements CategoryTreeDao {

	private static final int SQL_TIMEOUT = 5*60;

	private static final Logger logger = Logger.getLogger(CategoryTreeDaoJdbc.class);

	private DataSource marketDataSource;

    @SuppressWarnings("unchecked")
    public Map<CategoryTree.CategoryTreeNode, Integer> loadCategoryTree() {

        logger.debug("CategoryTreeDaoJdbc : loadCategoryTree()");
        return (Map) getJdbcTemplate().execute(new ConnectionCallback() {
            public Map<CategoryTree.CategoryTreeNode, Integer> doInConnection(Connection connection) throws SQLException, DataAccessException {
            	Map<CategoryTree.CategoryTreeNode, Integer> map = new HashMap<CategoryTree.CategoryTreeNode, Integer>();
                Statement statement = connection.createStatement();
                statement.setQueryTimeout(SQL_TIMEOUT);
                ResultSet rs = statement.executeQuery("select category, uniq_name, id, parent_id, hyper_id, not_used from categories");
                while (rs.next()) {
                    String name = rs.getString(1);
                    String uniqName = rs.getString(2);
                    int id = rs.getInt(3);
                    int parentId = rs.getInt(4);
                    int hyperId = rs.getInt(5);
                    boolean notUsed = rs.getBoolean("not_used");
                    CategoryTree.CategoryTreeNode node = new CategoryTree.CategoryTreeNode(name, uniqName, hyperId, id, !notUsed);
                    map.put(node, parentId);
                }
                return map;
            }
        });
    }

	@SuppressWarnings("unchecked")
	public Map<Integer, Pair<Integer, List<Integer>>> getHyperToMatcherMap() {
    	logger.debug("getHyperToMatcherMap()");
		return (Map) (new JdbcTemplate(marketDataSource)).execute(new ConnectionCallback() {
			public Map<Integer, Pair<Integer, List<Integer>>> doInConnection(Connection connection) throws SQLException, DataAccessException {
				Map<Integer, Pair<Integer, List<Integer>>> matching = new HashMap<Integer, Pair<Integer, List<Integer>>>();
				Statement statement = connection.createStatement();
				statement.setQueryTimeout(SQL_TIMEOUT);
				ResultSet rs = statement.executeQuery("select hyper_category_id, tovar_link, tovar_category_ids from MBOCategories");
				while(rs.next()) {
					String categories = rs.getString(3);
					List<Integer> list = new ArrayList<Integer>();
					if(categories != null) {
						StringTokenizer st = new StringTokenizer(categories);
						while(st.hasMoreTokens()) {
							list.add(Integer.valueOf(st.nextToken()));
						}
					}
					matching.put(rs.getInt(2), Pair.makePair(rs.getInt(1), list));
				}
				return matching;
			}
		});
	}

	public void setMarketDataSource(DataSource marketDataSource) {
		this.marketDataSource = marketDataSource;
	}
}
