package com.yangc.blog.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yangc.blog.bean.oracle.TBlogArticle;
import com.yangc.blog.service.ArticleService;
import com.yangc.blog.service.CommentService;
import com.yangc.blog.service.TagService;
import com.yangc.dao.BaseDao;
import com.yangc.dao.JdbcDao;

@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private BaseDao baseDao;
	@Autowired
	private JdbcDao jdbcDao;
	@Autowired
	private TagService tagService;
	@Autowired
	private CommentService commentService;

	@Override
	public void addOrUpdateArticle(Long articleId, String title, String content, Long categoryId, String tags) {
		TBlogArticle article = (TBlogArticle) this.baseDao.get(TBlogArticle.class, articleId);
		if (article == null) {
			article = new TBlogArticle();
		} else {
			this.tagService.delTagsByArticleId(articleId);
		}
		article.setTitle(title);
		article.setContent(content);
		article.setCategoryId(categoryId);
		this.baseDao.save(article);
		this.tagService.addTags(tags, article.getId());
	}

	@Override
	public void delArticle(Long articleId) {
		this.baseDao.updateOrDelete("delete TBlogArticle where id = ?", new Object[] { articleId });
		this.tagService.delTagsByArticleId(articleId);
		this.commentService.delCommentsByArticleId(articleId);
	}

	@Override
	public TBlogArticle getArticleById(Long articleId) {
		String sql = JdbcDao.SQL_MAPPING.get("blog.article.getArticleById");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("articleId", articleId);
		List<Map<String, Object>> mapList = this.jdbcDao.find(sql, paramMap);
		if (mapList == null || mapList.isEmpty()) return null;

		Map<String, Object> map = mapList.get(0);
		TBlogArticle article = new TBlogArticle();
		article.setId(MapUtils.getLong(map, "ID"));
		article.setTitle(MapUtils.getString(map, "TITLE"));
		article.setContent(MapUtils.getString(map, "CONTENT"));
		article.setCategoryId(MapUtils.getLong(map, "CATEGORY_ID"));
		article.setCategoryName(MapUtils.getString(map, "CATEGORY_NAME"));
		article.setTags(MapUtils.getString(map, "TAGS"));
		article.setCreateTimeStr(MapUtils.getString(map, "CREATE_TIME"));
		return article;
	}

	@Override
	public List<TBlogArticle> getArticleListBycategoryId_page(Long categoryId) {
		String sql = JdbcDao.SQL_MAPPING.get("blog.article.getArticleListBycategoryId_page");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("categoryId", categoryId);
		List<Map<String, Object>> mapList = this.jdbcDao.find(sql, paramMap);
		if (mapList == null || mapList.isEmpty()) return null;

		List<TBlogArticle> articleList = new ArrayList<TBlogArticle>();
		for (Map<String, Object> map : mapList) {
			TBlogArticle article = new TBlogArticle();
			article.setId(MapUtils.getLong(map, "ID"));
			article.setTitle(MapUtils.getString(map, "TITLE"));
			article.setCategoryName(MapUtils.getString(map, "CATEGORY_NAME"));
			article.setCreateTimeStr(MapUtils.getString(map, "CREATE_TIME"));
			articleList.add(article);
		}
		return articleList;
	}

	@Override
	public Long getArticleListBycategoryId_count(Long categoryId) {
		StringBuilder sb = new StringBuilder("select count(a) from TBlogArticle a where 1 = 1");
		Map<String, Object> paramMap = new HashMap<String, Object>();

		if (categoryId != null && categoryId.longValue() != 0) {
			sb.append(" and a.categoryId = :categoryId");
			paramMap.put("categoryId", categoryId);
		}

		Number count = (Number) this.baseDao.findAllByMap(sb.toString(), paramMap).get(0);
		return count.longValue();
	}

}
