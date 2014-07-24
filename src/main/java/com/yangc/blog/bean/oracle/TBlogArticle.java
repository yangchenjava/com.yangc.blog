package com.yangc.blog.bean.oracle;

import com.yangc.bean.BaseBean;

public class TBlogArticle extends BaseBean {

	private static final long serialVersionUID = 1077715033171707960L;

	private String title;
	private String content;
	private Long categoryId;
	private Long readCount;

	private String categoryName;
	private String tags;
	private Long commentCount;

	public TBlogArticle() {
	}

	public TBlogArticle(Long id, String title, Long readCount) {
		this.setId(id);
		this.title = title;
		this.readCount = readCount;
	}

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

	public Long getReadCount() {
		return readCount;
	}

	public void setReadCount(Long readCount) {
		this.readCount = readCount;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Long getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Long commentCount) {
		this.commentCount = commentCount;
	}

}
