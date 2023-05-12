package com.example.springboard.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.springboard.dto.CommentDTO;
import com.example.springboard.service.comment.CommentService;

@RestController
@RequestMapping("/comment")
public class CommentController {
	@Autowired
	private CommentService commentService;
	
	// 댓글 조회
//	@RequestMapping("/getComment")
//	public ModelAndView getComment(@PathVariable int boardNo) {
//		List<CommentDTO> commentList = commentService.getComment(boardNo);
//		
//		ModelAndView mv = new ModelAndView();
//		mv.setViewName("board/getBoard.html");
//		mv.addObject("commentList", commentList);
//		
//		return mv;
//	}
	
	@RequestMapping("/getComment")
	public ModelAndView getComment(CommentDTO commentDTO) {
		List<CommentDTO> commentList = commentService.getComment(commentDTO);
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("board/getBoard.html");
		mv.addObject("commentList", commentList);
		
		return mv;
	}
	
	// 댓글 작성
	@RequestMapping("/insertComment")
	public void insertComment(CommentDTO commentDTO,
			HttpServletResponse response, HttpServletRequest request) throws IOException {
		commentService.insertComment(commentDTO);
		
		response.sendRedirect("/board/board/" + commentDTO.getBoardNo()); 
	}
	
	// 댓글 삭제
	@DeleteMapping("/deleteComment")
	public void deleteComment(@RequestParam("commNo") int commNo) {
		commentService.deleteComment(commNo);
	}
	
}
