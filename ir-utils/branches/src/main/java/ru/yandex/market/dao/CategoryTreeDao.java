package ru.yandex.market.dao;

import java.util.List;
import java.util.Map;

import ru.yandex.market.CategoryTree;
import ru.yandex.utils.Pair;

public interface CategoryTreeDao {

	Map<CategoryTree.CategoryTreeNode, Integer> loadCategoryTree();

	Map<Integer, Pair<Integer, List<Integer>>> getHyperToMatcherMap();

}