package com.example.springboard.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.springboard.dto.CommentDTO;

@Mapper
public interface CommentMapper {
	void insertComment(CommentDTO commentDTO);
	
	List<CommentDTO> getComment(CommentDTO commentDTO);
//	List<CommentDTO> getComment(int boardNo);
	
	void deleteComment(int commNo);
}
