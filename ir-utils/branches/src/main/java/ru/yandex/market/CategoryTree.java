package ru.yandex.market;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.log4j.Logger;

import ru.yandex.market.dao.CategoryTreeDao;
import ru.yandex.utils.Pair;
import ru.yandex.utils.Reloadable;


public class CategoryTree implements Reloadable {
	private static final Logger logger = Logger.getLogger(CategoryTree.class);

	private CategoryTreeDao mapperDao;

	public static final int ROOT_TOVAR_CATEGORY_ID = 0;
	public static final int ROOT_CATEGORY_ID = 90401;

	//TODO: remove hack for tree
	public static final int BOOK_HYPER_CATEGORY_ID = 90829;
	public static final Integer BOOK_MATCHER_ID = 1045639;

	private final AtomicReference<Tree> tree;

	public CategoryTree() {
		tree = new AtomicReference<Tree>();
	}

	public static class CategoryTreeNode {
		private final String name;
		private final String uniqueName;
		private final Integer hyperId;
		private final Integer tovarId;
		private final boolean visible;

		private Integer matcherId;

		private List<Integer> linkedCategories;
		private List<CategoryTreeNode> children = new ArrayList<CategoryTreeNode>();

		private int height = -1;

		private CategoryTreeNode parent;

		public CategoryTreeNode(String name, String uniqName, Integer hyperId, Integer tovarId, boolean visible) {
			this.name = name;
			uniqueName = uniqName;

			this.hyperId = hyperId;
			this.tovarId = tovarId;
			this.visible = visible;
			if(hyperId == BOOK_HYPER_CATEGORY_ID) {
				matcherId = BOOK_MATCHER_ID;
				addLinkedCategory(matcherId);
			}
		}

		public Integer getMatcherId() {
			return matcherId;
		}

		public void setMatcherId(Integer matcherId) {
			this.matcherId = matcherId;
		}

		public Integer getHyperId() {
			return hyperId;
		}

		public Integer getTovarId() {
			return tovarId;
		}

		public CategoryTreeNode getParent() {
			return parent;
		}

		public Integer getParentId() {
			return parent != null? parent.getHyperId(): null;
		}

		public void setParent(CategoryTreeNode parent) {
			this.parent = parent;
		}

		public boolean isVisible() {
			return visible;
		}

		public boolean isChildOrSelf(int hyperId) {
			if(this.hyperId.intValue() == hyperId) {
				return true;
			}
			return parent != null? parent.isChildOrSelf(hyperId) : false;
		}

		@Override
		public int hashCode() {
			return hyperId.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if(obj == null || !(obj instanceof CategoryTreeNode)) {
				return false;
			}
			final CategoryTreeNode node = (CategoryTreeNode) obj;
			return hyperId.equals(node.hyperId);
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public List<Integer> getLinkedCategories() {
			return linkedCategories;
		}

		public void addLinkedCategory(Integer guruCategoryId) {
			if(linkedCategories == null) {
				linkedCategories = new ArrayList<Integer>();
			}
			linkedCategories.add(guruCategoryId);
		}

		public void addChildren(CategoryTreeNode node) {
			children.add(node);
		}

		public List<CategoryTreeNode> getChildren() {
			return children;
		}

		public String getName() {
			return name;
		}

		public String getUniqueName() {
			return uniqueName;
		}
	}

	public static class Tree {
		private Map<Integer, CategoryTreeNode> tree = new HashMap<Integer, CategoryTreeNode>();
		private Map<Integer, Integer> tovar2hyper = new HashMap<Integer, Integer>();

		public Tree(
		    Map<Integer, CategoryTreeNode> localTree,
		    Map<Integer, Integer> localTovar2hyper)
		{
			tree = localTree;
			tovar2hyper = localTovar2hyper;
		}

		public CategoryTreeNode getByTovarId(Integer tovarId) {
			Integer hyperId = tovar2hyper.get(tovarId);
			return hyperId != null ? tree.get(hyperId) : null;
		}

		public CategoryTreeNode getByHyperId(Integer hyperId) {
			return hyperId != null ? tree.get(hyperId) : null;
		}

		public int getHeight(Integer hyperId) {
			return hyperId != null ? tree.get(hyperId).height : -1;
		}

		public Set<Integer> getHyperCategoryIds() {
			return tree.keySet();
		}

		public Collection<CategoryTreeNode> getCategories() {
			return tree.values();
		}

		public CategoryTreeNode getRootCategory() {
			return tree.get(ROOT_CATEGORY_ID);
		}

		public int size() {
			return tree.size();
		}
	}

	private static void check(CategoryTreeNode node) {
		if(node.height != -1) {
			return;
		}
		if(node.getParent() != null) {
			check(node.getParent());
			node.height = node.getParent().height + 1;
			if(node.getParent().matcherId == BOOK_MATCHER_ID) {
				node.matcherId = BOOK_MATCHER_ID;
				node.addLinkedCategory(BOOK_MATCHER_ID);
			}
		} else {
			node.height = 0;
		}
	}

	public void reload() {
		logger.info("Category Tree reload was started");
		tree.set(create());
		logger.info("Category Tree reload was finished");
	}

	public Tree create() {
		Map<CategoryTreeNode, Integer> nodes = mapperDao.loadCategoryTree();
		Map<Integer, CategoryTreeNode> localTree = new HashMap<Integer, CategoryTreeNode>();
		Map<Integer, Integer> localTovar2hyper = new HashMap<Integer, Integer>();
		Map<Integer, Pair<Integer, List<Integer>>> localHyper2matcher = mapperDao.getHyperToMatcherMap();

		for(CategoryTreeNode key : nodes.keySet()) {
			localTree.put(key.hyperId, key);
			localTovar2hyper.put(key.tovarId, key.hyperId);
			Pair<Integer, List<Integer>> pair = localHyper2matcher.get(key.hyperId);
			if(pair != null) {
				key.setMatcherId(pair.getFirst());
				key.addLinkedCategory(pair.getFirst());
			}
		}
		for(Map.Entry<CategoryTreeNode, Integer> entity : nodes.entrySet()) {
			if(!entity.getKey().tovarId.equals(ROOT_TOVAR_CATEGORY_ID)) {
				entity.getKey().setParent(localTree.get(localTovar2hyper.get(entity.getValue())));
				localTree.get(localTovar2hyper.get(entity.getValue())).addChildren(entity.getKey());
			}
		}

		for(CategoryTreeNode node : nodes.keySet()) {
			// calculate height
			check(node);
		}
		for(Pair<Integer, List<Integer>> pair : localHyper2matcher.values()) {
			Integer guruCategoryId = pair.getFirst();
			for(Integer tovarId : pair.getSecond()) {
				CategoryTreeNode node = localTree.get(localTovar2hyper.get(tovarId));
				if(node != null) {
					node.addLinkedCategory(guruCategoryId);
				}
			}
		}
		return new Tree(localTree, localTovar2hyper);
	}

	public CategoryTreeNode getByTovarId(Integer tovarId) {
		return tree.get().getByTovarId(tovarId);
	}

	public CategoryTreeNode getByHyperId(Integer hyperId) {
		return tree.get().getByHyperId(hyperId);
	}

	public int getHeight(Integer hyperId) {
		return tree.get().getHeight(hyperId);
	}

	public Set<Integer> getHyperCategoryIds() {
		return tree.get().getHyperCategoryIds();
	}

	public Collection<CategoryTreeNode> getCategories() {
		return tree.get().getCategories();
	}

	public CategoryTreeNode getRootCategory() {
		return tree.get().getRootCategory();
	}

	public int size() {
		return tree.get().size();
	}

	public void setCategoryTreeDao(CategoryTreeDao mapperDao) {
		this.mapperDao = mapperDao;
	}
}
