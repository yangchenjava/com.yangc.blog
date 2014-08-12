package com.yangc.blog.resource;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yangc.bean.DataGridBean;
import com.yangc.bean.ResultBean;
import com.yangc.blog.bean.TBlogArticle;
import com.yangc.blog.service.ArticleService;
import com.yangc.exception.WebApplicationException;
import com.yangc.system.bean.Permission;

@Controller
@RequestMapping("/article")
public class ArticleResource {

	private static final Logger logger = Logger.getLogger(ArticleResource.class);

	@Autowired
	private ArticleService articleService;

	/**
	 * @功能: 查询所有文章(分页)
	 * @作者: yangc
	 * @创建日期: 2013年12月23日 下午5:30:25
	 * @return
	 */
	@RequestMapping(value = "getArticleListBycategoryId_page", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("article:" + Permission.SEL)
	public DataGridBean getArticleListBycategoryId_page(Long categoryId) {
		logger.info("getArticleListBycategoryId_page - categoryId=" + categoryId);
		List<TBlogArticle> articleList = this.articleService.getArticleListBycategoryId_page(categoryId);
		return new DataGridBean(articleList);
	}

	/**
	 * @功能: 根据id查询文章
	 * @作者: yangc
	 * @创建日期: 2013年12月23日 下午5:30:25
	 * @return
	 */
	@RequestMapping(value = "getArticle", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("article:" + Permission.SEL)
	public TBlogArticle getArticle(Long id) {
		logger.info("getArticle - id=" + id);
		return this.articleService.getArticleById(id);
	}

	/**
	 * @功能: 添加文章
	 * @作者: yangc
	 * @创建日期: 2013年12月23日 下午2:59:26
	 * @return
	 */
	@RequestMapping(value = "addArticle", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("article:" + Permission.ADD)
	public ResultBean addArticle(String title, String content, Long categoryId, String tags) {
		logger.info("addArticle - title=" + title + ", content=" + content + ", categoryId=" + categoryId + ", tags=" + tags);
		try {
			this.articleService.addOrUpdateArticle(null, title, content, categoryId, tags);
			return new ResultBean(true, "添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

	/**
	 * @功能: 修改文章
	 * @作者: yangc
	 * @创建日期: 2013年12月23日 下午2:59:26
	 * @return
	 */
	@RequestMapping(value = "updateArticle", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("article:" + Permission.UPD)
	public ResultBean updateArticle(Long id, String title, String content, Long categoryId, String tags) {
		logger.info("updateArticle - id=" + id + ", title=" + title + ", content=" + content + ", categoryId=" + categoryId + ", tags=" + tags);
		try {
			this.articleService.addOrUpdateArticle(id, title, content, categoryId, tags);
			return new ResultBean(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

	/**
	 * @功能: 删除文章
	 * @作者: yangc
	 * @创建日期: 2013年12月23日 下午3:02:44
	 * @return
	 */
	@RequestMapping(value = "delArticle", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("article:" + Permission.DEL)
	public ResultBean delArticle(Long id) {
		logger.info("delArticle - id=" + id);
		try {
			this.articleService.delArticle(id);
			return new ResultBean(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

}
