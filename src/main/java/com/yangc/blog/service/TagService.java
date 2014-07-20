package com.yangc.blog.service;

public interface TagService {

	public void addTags(String tags, Long articleId);

	public void delTagsByArticleId(Long articleId);

}
