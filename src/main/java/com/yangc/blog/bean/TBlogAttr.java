package com.yangc.blog.bean;

import com.yangc.bean.BaseBean;

public class TBlogAttr extends BaseBean {

	private static final long serialVersionUID = 242750327315018583L;

	private String attrName;
	private String attrValue;
	private String description;

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
