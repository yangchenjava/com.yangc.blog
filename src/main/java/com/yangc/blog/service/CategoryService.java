package com.yangc.blog.service;

import java.util.List;

import com.yangc.blog.bean.CategoryTree;
import com.yangc.blog.bean.TBlogCategory;

public interface CategoryService {

	public void addOrUpdateCategory(Long categoryId, String categoryName, Long serialNum, Long parentCategoryId);

	public void updateParentCategoryId(Long categoryId, Long parentCategoryId);

	public void delCategory(Long categoryId) throws IllegalStateException;

	public List<CategoryTree> getCategoryTreeListByParentCategoryId(Long parentCategoryId);

	public List<TBlogCategory> getCategoryListSameLevel();

	public List<TBlogCategory> getCategoryListDiffLevel();

}
