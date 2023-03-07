package com.model2.mvc.service.comment;

import java.util.List;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Comment;

public interface CommentDao {

	public void insertComment(Comment comment) throws Exception;
	
	public Comment findComment(int comNo) throws Exception;
	
	public Comment findCommentByTranNo(int tranNo) throws Exception;
	
	public void updateCommentDetail(Comment comment) throws Exception;
	
	public List<Object> getCommentList(Search search, int prodNo) throws Exception;
	
	public int getTotalCount(int prodNo) throws Exception;
}
