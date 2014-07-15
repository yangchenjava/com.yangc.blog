package com.yangc.blog.service;

import java.util.List;

import com.yangc.blog.bean.oracle.TBlogAttr;

public interface AttrService {

	public void addOrUpdateAttr(Long attrId, String attrName, String attrValue, String description);

	public void delAttr(Long attrId);

	public List<TBlogAttr> getAttrList();

}
