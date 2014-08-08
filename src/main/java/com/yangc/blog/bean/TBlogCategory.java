package com.yangc.blog.bean;

import java.util.List;

import com.yangc.bean.BaseBean;

public class TBlogCategory extends BaseBean {

	private static final long serialVersionUID = -1260736314257794529L;

	private String categoryName;
	private Long parentCategoryId;
	private Long serialNum;

	private List<TBlogCategory> childRenCategory;

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Long getParentCategoryId() {
		return parentCategoryId;
	}

	public void setParentCategoryId(Long parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	public Long getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(Long serialNum) {
		this.serialNum = serialNum;
	}

	public List<TBlogCategory> getChildRenCategory() {
		return childRenCategory;
	}

	public void setChildRenCategory(List<TBlogCategory> childRenCategory) {
		this.childRenCategory = childRenCategory;
	}

}