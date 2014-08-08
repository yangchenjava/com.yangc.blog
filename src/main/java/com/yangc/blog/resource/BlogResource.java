package com.yangc.blog.resource;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yangc.bean.DataGridBean;
import com.yangc.bean.ResultBean;
import com.yangc.blog.bean.TBlogArticle;
import com.yangc.blog.bean.TBlogAttr;
import com.yangc.blog.bean.TBlogCategory;
import com.yangc.blog.bean.TBlogComment;
import com.yangc.blog.bean.TBlogTag;
import com.yangc.blog.service.ArticleService;
import com.yangc.blog.service.AttrService;
import com.yangc.blog.service.CategoryService;
import com.yangc.blog.service.CommentService;
import com.yangc.blog.service.TagService;
import com.yangc.exception.WebApplicationException;
import com.yangc.utils.net.IpUtils;

@Controller
@RequestMapping("/blog")
public class BlogResource {

	private static final Logger logger = Logger.getLogger(BlogResource.class);

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private AttrService attrService;
	@Autowired
	private TagService tagService;
	@Autowired
	private CommentService commentService;

	@RequestMapping(value = "getCategoryList", method = RequestMethod.POST)
	@ResponseBody
	public List<TBlogCategory> getCategoryList() {
		logger.info("getCategoryList");
		return this.categoryService.getCategoryListDiffLevel();
	}

	@RequestMapping(value = "getArticleList_page", method = RequestMethod.POST)
	@ResponseBody
	public DataGridBean getArticleList_page(String title, Long categoryId, String tag) {
		logger.info("getArticleList_page");
		List<TBlogArticle> articleList = this.articleService.getArticleList_page(title, categoryId, tag);
		return new DataGridBean(articleList);
	}

	@RequestMapping(value = "getArticleListByReadCount", method = RequestMethod.POST)
	@ResponseBody
	public List<TBlogArticle> getArticleListByReadCount() {
		logger.info("getArticleListByReadCount");
		return this.articleService.getArticleListByReadCount();
	}

	@RequestMapping(value = "getArticleById", method = RequestMethod.POST)
	@ResponseBody
	public TBlogArticle getArticleById(Long id) {
		logger.info("getArticleById");
		return this.articleService.getArticleById(id, 0);
	}

	@RequestMapping(value = "getAttrList", method = RequestMethod.POST)
	@ResponseBody
	public List<TBlogAttr> getAttrList() {
		logger.info("getAttrList");
		return this.attrService.getAttrList();
	}

	@RequestMapping(value = "getTags", method = RequestMethod.POST)
	@ResponseBody
	public List<TBlogTag> getTags() {
		logger.info("getTags");
		return this.tagService.getTags();
	}

	@RequestMapping(value = "addOrUpdateComment", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean addOrUpdateComment(String name, String content, Long articleId, HttpServletRequest request) {
		logger.info("addOrUpdateComment");
		try {
			this.commentService.addOrUpdateComment(null, name, content, articleId, IpUtils.getIpAddress(request));
			return new ResultBean(true, "添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

	@RequestMapping(value = "getCommentList", method = RequestMethod.POST)
	@ResponseBody
	public List<TBlogComment> getCommentList(Long articleId) {
		logger.info("getCommentList");
		return this.commentService.getCommentListByArticleId(articleId);
	}

}
