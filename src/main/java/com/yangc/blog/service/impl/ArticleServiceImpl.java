package com.yangc.blog.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yangc.blog.bean.TBlogArticle;
import com.yangc.blog.service.ArticleService;
import com.yangc.blog.service.CommentService;
import com.yangc.blog.service.TagService;
import com.yangc.blog.utils.Constants;
import com.yangc.common.Pagination;
import com.yangc.common.PaginationThreadUtils;
import com.yangc.dao.BaseDao;
import com.yangc.dao.JdbcDao;
import com.yangc.utils.lang.HtmlFilterUtils;

@Service
@SuppressWarnings("unchecked")
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
			article.setReadCount(0L);
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
	public TBlogArticle getArticleById(Long articleId, int foreOrBack) {
		// 如果前台访问文章, 文章阅读次数加1
		if (foreOrBack == 0) {
			this.baseDao.updateOrDelete("update TBlogArticle set readCount = readCount + 1 where id = ?", new Object[] { articleId });
		}

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
		article.setReadCount(MapUtils.getLong(map, "READ_COUNT"));
		article.setCommentCount(MapUtils.getLong(map, "COMMENT_COUNT"));
		article.setPrevId(MapUtils.getLong(map, "PREV_ID"));
		article.setPrevTitle(MapUtils.getString(map, "PREV_TITLE"));
		article.setNextId(MapUtils.getLong(map, "NEXT_ID"));
		article.setNextTitle(MapUtils.getString(map, "NEXT_TITLE"));
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
			article.setReadCount(MapUtils.getLong(map, "READ_COUNT"));
			article.setCommentCount(MapUtils.getLong(map, "COMMENT_COUNT"));
			article.setCreateTimeStr(MapUtils.getString(map, "CREATE_TIME"));
			articleList.add(article);
		}
		return articleList;
	}

	@Override
	public Long getArticleListBycategoryId_count(Long categoryId) {
		StringBuilder sb = new StringBuilder("select count(a) from TBlogArticle a where 1 = 1");
		Map<String, Object> paramMap = new HashMap<String, Object>();

		if (categoryId != null && categoryId != 0) {
			sb.append(" and a.categoryId = :categoryId");
			paramMap.put("categoryId", categoryId);
		}

		Number count = (Number) this.baseDao.findAllByMap(sb.toString(), paramMap).get(0);
		return count.longValue();
	}

	@Override
	public List<TBlogArticle> getArticleListByReadCount() {
		// 只取阅读次数最多的前十篇文章
		Pagination pagination = new Pagination();
		pagination.setPageNow(1);
		pagination.setPageSize(10);
		PaginationThreadUtils.set(pagination);
		return this.baseDao.find("select new TBlogArticle(id, title, readCount) from TBlogArticle order by readCount desc", null);
	}

	@Override
	public List<TBlogArticle> getArticleList_page(String title, Long categoryId, String tag) {
		String sql = JdbcDao.SQL_MAPPING.get("blog.article.getArticleList_page");

		StringBuilder sb = new StringBuilder("where 1 = 1");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(title)) {
			sb.append(" and a.title LIKE :title");
			paramMap.put("title", "%" + title + "%");
		}
		if (categoryId != null && categoryId != 0) {
			sb.append(" and a.category_id = :categoryId");
			paramMap.put("categoryId", categoryId);
		}
		if (StringUtils.isNotBlank(tag)) {
			sb.append(" and b.tags LIKE :tag");
			paramMap.put("tag", "%" + tag + "%");
		}
		if (paramMap.isEmpty()) {
			sql = sql.replace("${condition}", "");
		} else {
			sql = sql.replace("${condition}", sb.toString().replace("1 = 1 and", ""));
		}

		List<Map<String, Object>> mapList = this.jdbcDao.find(sql, paramMap);
		if (mapList == null || mapList.isEmpty()) return null;

		List<TBlogArticle> articleList = new ArrayList<TBlogArticle>();
		for (Map<String, Object> map : mapList) {
			TBlogArticle article = new TBlogArticle();
			article.setId(MapUtils.getLong(map, "ID"));
			article.setTitle(MapUtils.getString(map, "TITLE"));
			String content = HtmlFilterUtils.filterHtml(MapUtils.getString(map, "CONTENT"));
			article.setContent(StringUtils.length(content) <= Constants.ARTICLE_SUMMARY_LIMIT ? content : content.substring(0, Constants.ARTICLE_SUMMARY_LIMIT) + "...");
			article.setTags(MapUtils.getString(map, "TAGS"));
			article.setCreateTimeStr(MapUtils.getString(map, "CREATE_TIME"));
			article.setReadCount(MapUtils.getLong(map, "READ_COUNT"));
			article.setCommentCount(MapUtils.getLong(map, "COMMENT_COUNT"));
			articleList.add(article);
		}
		return articleList;
	}

}
