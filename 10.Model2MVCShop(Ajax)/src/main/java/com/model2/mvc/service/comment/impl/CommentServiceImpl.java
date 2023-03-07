package com.model2.mvc.service.comment.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.comment.CommentDao;
import com.model2.mvc.service.comment.CommentService;
import com.model2.mvc.service.domain.Comment;

@Service("commentServiceImpl")
public class CommentServiceImpl implements CommentService {

	@Autowired
	@Qualifier("commentDaoImpl")
	private CommentDao commentDao;

	public CommentServiceImpl() {
		System.out.println("CommentServiceImpl default »ý¼ºÀÚ");
	}
	
	@Override
	public Comment addComment(Comment comment) throws Exception {
		commentDao.insertComment(comment);
		return comment;
	}

	@Override
	public Comment getComment(int comNo) throws Exception {
		return commentDao.findComment(comNo);
	}

	@Override
	public Comment getCommentByTranNo(int tranNo) throws Exception {
		return commentDao.findCommentByTranNo(tranNo);
	}

	@Override
	public Map<String, Object> getCommentList(Search search, int prodNo) throws Exception {
		List<Object> list = commentDao.getCommentList(search, prodNo);
		int totalCount = commentDao.getTotalCount(prodNo);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("totalCount", totalCount);
		return map;
	}

	@Override
	public Comment updateComment(Comment comment) throws Exception {
		commentDao.updateCommentDetail(comment);
		return comment;
	}

}
