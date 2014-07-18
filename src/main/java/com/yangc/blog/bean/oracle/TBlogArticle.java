package com.yangc.blog.bean.oracle;

import com.yangc.bean.BaseBean;

public class TBlogArticle extends BaseBean {

	private static final long serialVersionUID = 1077715033171707960L;

	private String title;
	private String content;
	private Long categoryId;

	private String categoryName;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

}
