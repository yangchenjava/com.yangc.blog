package com.yangc.blog.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yangc.blog.bean.TBlogTag;
import com.yangc.blog.service.TagService;
import com.yangc.dao.BaseDao;
import com.yangc.dao.JdbcDao;

@Service
@SuppressWarnings("unchecked")
public class TagServiceImpl implements TagService {

	@Autowired
	private BaseDao baseDao;
	@Autowired
	private JdbcDao jdbcDao;

	@Override
	public void addTags(String tags, Long articleId) {
		if (StringUtils.isNotBlank(tags)) {
			String sql = JdbcDao.SQL_MAPPING.get("blog.tag.addTags");
			List<Object[]> paramList = new ArrayList<Object[]>();
			for (String tag : tags.replaceAll("，", ",").split(",")) {
				paramList.add(new Object[] { tag, articleId });
			}
			this.jdbcDao.batchExecute(sql, paramList);
		}
	}

	@Override
	public void delTagsByArticleId(Long articleId) {
		this.baseDao.updateOrDelete("delete TBlogTag where articleId = ?", new Object[] { articleId });
	}

	@Override
	public List<TBlogTag> getTags() {
		List<Object[]> list = this.baseDao.findAll("select tagName, count(tagName) from TBlogTag group by tagName", null);
		if (list == null || list.isEmpty()) return null;

		List<TBlogTag> tags = new ArrayList<TBlogTag>();
		for (Object[] objs : list) {
			TBlogTag tag = new TBlogTag();
			tag.setTagName((String) objs[0]);
			tag.setArticleCount(((Number) objs[1]).longValue());
			tags.add(tag);
		}
		return tags;
	}

}
