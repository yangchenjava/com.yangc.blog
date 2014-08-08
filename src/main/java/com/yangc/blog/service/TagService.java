package com.yangc.blog.service;

import java.util.List;

import com.yangc.blog.bean.TBlogTag;

public interface TagService {

	public void addTags(String tags, Long articleId);

	public void delTagsByArticleId(Long articleId);

	public List<TBlogTag> getTags();

}
