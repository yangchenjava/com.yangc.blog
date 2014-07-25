package com.yangc.blog.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yangc.blog.bean.oracle.TBlogComment;
import com.yangc.blog.service.CommentService;
import com.yangc.dao.BaseDao;
import com.yangc.dao.JdbcDao;
import com.yangc.utils.BeanUtils;

@Service
@SuppressWarnings("unchecked")
public class CommentServiceImpl implements CommentService {

	@Autowired
	private BaseDao baseDao;
	@Autowired
	private JdbcDao jdbcDao;

	@Override
	public void addOrUpdateComment(Long commentId, String name, String content, Long articleId, String ipAddress) {
		TBlogComment comment = (TBlogComment) this.baseDao.get(TBlogComment.class, commentId);
		if (comment == null) {
			comment = new TBlogComment();
		}
		comment.setName(name);
		comment.setContent(content);
		comment.setArticleId(articleId);
		comment.setIpAddress(ipAddress);
		this.baseDao.save(comment);
	}

	@Override
	public void delComments(String commentIds) {
		if (StringUtils.isNotBlank(commentIds)) {
			this.baseDao.updateOrDelete("delete TBlogComment where id in (" + commentIds + ")", null);
		}
	}

	@Override
	public void delCommentsByArticleId(Long articleId) {
		this.baseDao.updateOrDelete("delete TBlogComment where articleId = ?", new Object[] { articleId });
	}

	@Override
	public List<TBlogComment> getCommentListByArticleId(Long articleId) {
		List<TBlogComment> commentList = this.baseDao.findAll("from TBlogComment where articleId = ? order by id desc", new Object[] { articleId });
		return BeanUtils.fillingTime(commentList);
	}

	@Override
	public List<TBlogComment> getCommentList_page() {
		String sql = JdbcDao.SQL_MAPPING.get("blog.comment.getCommentList_page");
		List<Map<String, Object>> mapList = this.jdbcDao.find(sql, null);
		if (mapList == null || mapList.isEmpty()) return null;

		List<TBlogComment> commentList = new ArrayList<TBlogComment>();
		for (Map<String, Object> map : mapList) {
			TBlogComment comment = new TBlogComment();
			comment.setId(MapUtils.getLong(map, "ID"));
			comment.setName(MapUtils.getString(map, "NAME"));
			comment.setContent(MapUtils.getString(map, "CONTENT"));
			comment.setArticleId(MapUtils.getLong(map, "ARTICLE_ID"));
			comment.setArticleTitle(MapUtils.getString(map, "TITLE"));
			comment.setIpAddress(MapUtils.getString(map, "IP_ADDRESS"));
			comment.setCreateTimeStr(MapUtils.getString(map, "CREATE_TIME"));
			commentList.add(comment);
		}
		return commentList;
	}

}
