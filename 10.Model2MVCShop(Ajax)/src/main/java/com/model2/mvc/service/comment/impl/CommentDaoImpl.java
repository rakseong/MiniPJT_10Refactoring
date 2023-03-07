package com.model2.mvc.service.comment.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.comment.CommentDao;
import com.model2.mvc.service.domain.Comment;

@Repository("commentDaoImpl")
public class CommentDaoImpl implements CommentDao {

	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSession sqlSession;
	
	public CommentDaoImpl() {
		System.out.println("CommentDaoImpl default »ý¼ºÀÚ");
	}

	public void setSqlSession(SqlSession sqlSession) {
		System.out.println("ProductDaoImpl setter");
		this.sqlSession = sqlSession;
	}
	
	@Override
	public void insertComment(Comment comment) throws Exception {
		sqlSession.insert("CommentMapper.addComment",comment);
	}

	@Override
	public Comment findComment(int comNo) throws Exception {
		return sqlSession.selectOne("CommentMapper.getComment",comNo);
	}

	@Override
	public Comment findCommentByTranNo(int tranNo) throws Exception {
		return sqlSession.selectOne("CommentMapper.getCommentByTranNo",tranNo);
	}

	@Override
	public void updateCommentDetail(Comment comment) throws Exception {
		sqlSession.update("CommentMapper.updateComment",comment);
	}

	@Override
	public List<Object> getCommentList(Search search, int prodNo) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("search",search);
		map.put("prodNo", prodNo);
		return sqlSession.selectList("CommentMapper.getCommentList",map);
	}

	@Override
	public int getTotalCount(int prodNo) throws Exception {
		return sqlSession.selectOne("CommentMapper.getTotalCount",prodNo);
	}

}
