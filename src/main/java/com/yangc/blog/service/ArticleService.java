package com.yangc.blog.service;

import java.util.List;

import com.yangc.blog.bean.oracle.TBlogArticle;

public interface ArticleService {

	public void addOrUpdateArticle(Long articleId, String title, String content, Long categoryId, String tags);

	public void delArticle(Long articleId);

	public TBlogArticle getArticleById(Long articleId);

	public List<TBlogArticle> getArticleListBycategoryId_page(Long categoryId);

	public Long getArticleListBycategoryId_count(Long categoryId);

}
