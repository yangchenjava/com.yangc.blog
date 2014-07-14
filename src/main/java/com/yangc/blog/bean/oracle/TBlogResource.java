package com.yangc.blog.bean.oracle;

import com.yangc.bean.BaseBean;

public class TBlogResource extends BaseBean {

	private static final long serialVersionUID = 5081491924378349740L;

	private String resourceName;
	private String saveName;
	private Long resourceSize;
	private Long articleId;

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getSaveName() {
		return saveName;
	}

	public void setSaveName(String saveName) {
		this.saveName = saveName;
	}

	public Long getResourceSize() {
		return resourceSize;
	}

	public void setResourceSize(Long resourceSize) {
		this.resourceSize = resourceSize;
	}

	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

}
