package com.yangc.blog.bean.oracle;

import com.yangc.bean.BaseBean;

public class TBlogCategory extends BaseBean {

	private static final long serialVersionUID = -1260736314257794529L;

	private String categoryName;
	private Long parentCategoryId;
	private Long serialNum;

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

}