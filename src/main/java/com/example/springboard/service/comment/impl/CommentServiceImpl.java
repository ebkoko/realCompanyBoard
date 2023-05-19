package com.example.springboard.service.comment.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboard.dto.CommentDTO;
import com.example.springboard.mapper.CommentMapper;
import com.example.springboard.service.comment.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
	@Autowired
	private CommentMapper commentMapper;
	
	@Override
	public void insertComment(CommentDTO commentDTO) {
		commentMapper.insertComment(commentDTO);
	}
	
	@Override
	public List<CommentDTO> getComment(CommentDTO commentDTO) {
		return commentMapper.getComment(commentDTO);
	}
//	@Override
//	public List<CommentDTO> getComment(int boardNo) {
//		return commentMapper.getComment(boardNo);
//	}
	
	@Override
	public void deleteComment(int commNo) {
		commentMapper.deleteComment(commNo);
	}
	
	@Override
	public void insertReply(CommentDTO commentDTO) {
		commentMapper.insertReply(commentDTO);
	}
	
	@Override
	public int replyCheck(CommentDTO commentDTO) {
		return commentMapper.replyCheck(commentDTO);
	}
	
	@Override
	public void updateComm(CommentDTO commentDTO) {
		commentMapper.updateComm(commentDTO);
	}
}
