package com.yangc.blog.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yangc.blog.bean.TBlogAttr;
import com.yangc.blog.service.AttrService;
import com.yangc.dao.BaseDao;

@Service
@SuppressWarnings("unchecked")
public class AttrServiceImpl implements AttrService {

	@Autowired
	private BaseDao baseDao;

	@Override
	public void addOrUpdateAttr(Long attrId, String attrName, String attrValue, String description) {
		TBlogAttr attr = (TBlogAttr) this.baseDao.get(TBlogAttr.class, attrId);
		if (attr == null) {
			attr = new TBlogAttr();
		}
		attr.setAttrName(attrName);
		attr.setAttrValue(attrValue);
		attr.setDescription(description);
		this.baseDao.saveOrUpdate(attr);
	}

	@Override
	public void delAttr(Long attrId) {
		this.baseDao.updateOrDelete("delete TBlogAttr where id = ?", new Object[] { attrId });
	}

	@Override
	public List<TBlogAttr> getAttrList() {
		return this.baseDao.findAll("from TBlogAttr order by id", null);
	}

}
