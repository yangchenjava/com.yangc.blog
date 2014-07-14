package com.yangc.blog.bean.oracle;

import com.yangc.bean.BaseBean;

public class TBlogTag extends BaseBean {

	private static final long serialVersionUID = -3984379780162731462L;

	private String tagName;
	private Long articleId;

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

}
