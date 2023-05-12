package com.example.springboard.service.comment;

import java.util.List;

import com.example.springboard.dto.CommentDTO;

public interface CommentService {
	void insertComment(CommentDTO commentDTO);
	
	List<CommentDTO> getComment(CommentDTO commentDTO);
//	List<CommentDTO> getComment(int boardNo);
	
	void deleteComment(int commNo);
}
