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
import com.yangc.blog.bean.oracle.TBlogComment;
import com.yangc.blog.service.CommentService;
import com.yangc.exception.WebApplicationException;
import com.yangc.system.bean.oracle.Permission;

@Controller
@RequestMapping("/comment")
public class CommentResource {

	private static final Logger logger = Logger.getLogger(CommentResource.class);

	@Autowired
	private CommentService commentService;

	/**
	 * @功能: 查询所有评论
	 * @作者: yangc
	 * @创建日期: 2013年12月23日 下午2:13:04
	 * @return
	 */
	@RequestMapping(value = "getCommentList_page", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("comment:" + Permission.SEL)
	public DataGridBean getCommentList_page() {
		logger.info("getCommentList_page");
		List<TBlogComment> commentList = this.commentService.getCommentList_page();
		return new DataGridBean(commentList);
	}

	/**
	 * @功能: 删除评论
	 * @作者: yangc
	 * @创建日期: 2013年12月23日 下午3:02:44
	 * @return
	 */
	@RequestMapping(value = "delComments", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("comment:" + Permission.DEL)
	public ResultBean delComments(String commentIds) {
		logger.info("delComments - commentIds=" + commentIds);
		try {
			this.commentService.delComments(commentIds);
			return new ResultBean(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

}
