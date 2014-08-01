package com.yangc.blog.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yangc.blog.bean.oracle.CategoryTree;
import com.yangc.blog.bean.oracle.TBlogCategory;
import com.yangc.blog.service.ArticleService;
import com.yangc.blog.service.CategoryService;
import com.yangc.dao.BaseDao;
import com.yangc.dao.JdbcDao;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private BaseDao baseDao;
	@Autowired
	private JdbcDao jdbcDao;
	@Autowired
	private ArticleService articleService;

	@Override
	public void addOrUpdateCategory(Long categoryId, String categoryName, Long serialNum, Long parentCategoryId) {
		TBlogCategory category = (TBlogCategory) this.baseDao.get(TBlogCategory.class, categoryId);
		if (category == null) {
			category = new TBlogCategory();
		}
		category.setCategoryName(categoryName);
		category.setSerialNum(serialNum);
		category.setParentCategoryId(parentCategoryId);
		this.baseDao.save(category);
	}

	@Override
	public void updateParentCategoryId(Long categoryId, Long parentCategoryId) {
		this.baseDao.updateOrDelete("update TBlogCategory set parentCategoryId = ? where id = ?", new Object[] { parentCategoryId, categoryId });
	}

	@Override
	public void delCategory(Long categoryId) throws IllegalStateException {
		int totalCount = this.baseDao.getCount("select count(c) from TBlogCategory c where c.parentCategoryId = ?", new Object[] { categoryId });
		if (totalCount > 0) {
			throw new IllegalStateException("该类别下存在子类别");
		}
		if (this.articleService.getArticleListBycategoryId_count(categoryId) > 0) {
			throw new IllegalStateException("该类别下存在文章");
		}
		this.baseDao.updateOrDelete("delete TBlogCategory where id = ?", new Object[] { categoryId });
	}

	@Override
	public List<CategoryTree> getCategoryTreeListByParentCategoryId(Long parentCategoryId) {
		String sql = JdbcDao.SQL_MAPPING.get("blog.category.getCategoryTreeListByParentCategoryId");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("parentCategoryId", parentCategoryId);
		List<Map<String, Object>> mapList = this.jdbcDao.findAll(sql, paramMap);
		if (mapList == null || mapList.isEmpty()) return null;

		List<CategoryTree> categoryTreeList = new ArrayList<CategoryTree>();
		for (Map<String, Object> map : mapList) {
			CategoryTree categoryTree = new CategoryTree();
			categoryTree.setLeaf(MapUtils.getLongValue(map, "TOTALCOUNT") == 0);
			categoryTree.setCategoryId(MapUtils.getLong(map, "ID"));
			categoryTree.setCategoryName(MapUtils.getString(map, "CATEGORY_NAME"));
			categoryTree.setSerialNum(MapUtils.getLong(map, "SERIAL_NUM"));
			categoryTree.setParentCategoryId(parentCategoryId);
			categoryTreeList.add(categoryTree);
		}
		return categoryTreeList;
	}

	@Override
	public List<TBlogCategory> getCategoryListSameLevel() {
		String sql = JdbcDao.SQL_MAPPING.get("blog.category.getCategoryList");
		List<Map<String, Object>> mapList = this.jdbcDao.findAll(sql, null);
		if (mapList == null || mapList.isEmpty()) return null;

		List<TBlogCategory> categoryList = new ArrayList<TBlogCategory>();
		for (Map<String, Object> map : mapList) {
			TBlogCategory category = new TBlogCategory();
			category.setId(MapUtils.getLong(map, "ID"));
			category.setCategoryName(MapUtils.getString(map, "CATEGORY_NAME"));
			category.setParentCategoryId(MapUtils.getLong(map, "PARENT_CATEGORY_ID"));
			categoryList.add(category);
		}
		return categoryList;
	}

	@Override
	public List<TBlogCategory> getCategoryListDiffLevel() {
		String sql = JdbcDao.SQL_MAPPING.get("blog.category.getCategoryList");
		List<Map<String, Object>> mapList = this.jdbcDao.findAll(sql, null);
		if (mapList == null || mapList.isEmpty()) return null;

		Map<Long, TBlogCategory> tempMap = new LinkedHashMap<Long, TBlogCategory>();
		for (Map<String, Object> map : mapList) {
			Long id = MapUtils.getLong(map, "ID");
			String categoryName = MapUtils.getString(map, "CATEGORY_NAME");
			Long pid = MapUtils.getLong(map, "PARENT_CATEGORY_ID");

			if (pid == 0) {
				TBlogCategory category = new TBlogCategory();
				category.setId(id);
				category.setCategoryName(categoryName);
				category.setParentCategoryId(pid);
				category.setChildRenCategory(new ArrayList<TBlogCategory>());
				tempMap.put(id, category);
			} else {
				TBlogCategory parentCategory = tempMap.get(pid);
				if (parentCategory == null) continue;
				TBlogCategory category = new TBlogCategory();
				category.setId(id);
				category.setCategoryName(categoryName);
				category.setParentCategoryId(pid);

				List<TBlogCategory> childRenCategory = parentCategory.getChildRenCategory();
				childRenCategory.add(category);
				parentCategory.setChildRenCategory(childRenCategory);
			}
		}

		List<TBlogCategory> categoryList = new ArrayList<TBlogCategory>();
		for (Entry<Long, TBlogCategory> entry : tempMap.entrySet()) {
			categoryList.add(entry.getValue());
		}
		return categoryList;
	}

}
