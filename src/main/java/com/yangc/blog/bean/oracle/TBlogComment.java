package com.yangc.blog.bean.oracle;

import com.yangc.bean.BaseBean;

/**
 * 创建日期: 2014年7月20日 下午5:30:56
 * Title: 输电线路状态监测系统
 * Description: 对本文件的详细描述，原则上不能少于50字
 * @author yangc
 * @version 1.0
 */
/**
 * 创建日期: 2014年7月20日 下午5:30:58 Title: 输电线路状态监测系统 Description: 对本文件的详细描述，原则上不能少于50字
 * @author yangc
 * @version 1.0
 */
public class TBlogComment extends BaseBean {

	private static final long serialVersionUID = -2591851257035032954L;

	private String name;
	private String content;
	private Long articleId;
	private String ipAddress;

	private String articleTitle;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getArticleTitle() {
		return articleTitle;
	}

	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}

}
