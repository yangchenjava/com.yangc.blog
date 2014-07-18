package com.yangc.blog.bean.oracle;

import com.yangc.bean.AsyncTreeNode;

public class CategoryTree extends AsyncTreeNode {

	private Long categoryId;
	private String categoryName;
	private Long parentCategoryId;
	private Long serialNum;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.setId("" + categoryId);
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.setText(categoryName);
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

}
