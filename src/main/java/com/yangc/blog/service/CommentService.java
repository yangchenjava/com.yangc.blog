package com.yangc.blog.service;

import java.util.List;

import com.yangc.blog.bean.oracle.TBlogComment;

public interface CommentService {

	public void addOrUpdateComment(Long commentId, String name, String content, Long articleId, String ipAddress);

	public void delComments(String commentIds);

	public void delCommentsByArticleId(Long articleId);

	public List<TBlogComment> getCommentListByArticleId(Long articleId);

	public List<TBlogComment> getCommentList_page();

}
