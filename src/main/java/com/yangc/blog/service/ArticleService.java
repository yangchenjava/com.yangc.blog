package com.yangc.blog.service;

import java.util.List;

import com.yangc.blog.bean.TBlogArticle;

public interface ArticleService {

	public void addOrUpdateArticle(Long articleId, String title, String content, Long categoryId, String tags);

	public void delArticle(Long articleId);

	public TBlogArticle getArticleById(Long articleId, int foreOrBack);

	public List<TBlogArticle> getArticleListBycategoryId_page(Long categoryId);

	public Long getArticleListBycategoryId_count(Long categoryId);

	public List<TBlogArticle> getArticleListByReadCount();

	public List<TBlogArticle> getArticleList_page(String title, Long categoryId, String tag);

}
