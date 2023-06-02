package com.example.springboard.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.springboard.dto.CommentDTO;
import com.example.springboard.dto.ResponseDTO;
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
			@RequestParam("pageNum") int pageNum, @RequestParam("amount") int amount,
			@RequestParam("searchCondition") String searchCondition, @RequestParam("searchKeyword") String searchKeyword,
			HttpServletResponse response, HttpServletRequest request) throws IOException {
		commentService.insertComment(commentDTO);
		
		response.sendRedirect("/board/board/" + commentDTO.getBoardNo() + "?pageNum=" + pageNum + "&amount=" + amount + "&searchCondition=" + searchCondition + "&searchKeyword=" + searchKeyword); 
	}
	
	// 댓글 삭제
	@DeleteMapping("/deleteComment")
	public void deleteComment(@RequestParam("commNo") int commNo) {
		commentService.deleteComment(commNo);
		
	}
	
	@PostMapping("/replyCheck")
	public ResponseEntity<?> replyCheck(@RequestParam("commNo") int commNo, @RequestParam("commOr") int commOr,
			@RequestParam("commGr") int commGr, @RequestParam("boardNo") int boardNo, CommentDTO commentDTO) {
		ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
		Map<String, String> returnMap = new HashMap<String, String>();
		
		try {			
			int checkedReply = commentService.replyCheck(commentDTO);
			
			// 댓글인지 답글인지 확인(댓글: 0, 답글: 1)
			if(commOr != 0) {
				returnMap.put("msg", "isReply");
			} else {
				
				// 답글 있는지 확인(답글이 있으면 checkedReply > 1)
				if(checkedReply > 1) {
										
					returnMap.put("msg", "existReply");
					
				} else {
					
					returnMap.put("msg", "noReply");
				}
			}
			
			responseDTO.setItem(returnMap);
			
			return ResponseEntity.ok().body(responseDTO);
		} catch(Exception e) {
			responseDTO.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	
	@PostMapping("/updateComment")
	public void updateComment(@RequestParam("commNo") int commNo, CommentDTO commentDTO,
			@RequestParam("pageNum") int pageNum, @RequestParam("amount") int amount,
			@RequestParam("searchCondition") String searchCondition, @RequestParam("searchKeyword") String searchKeyword,
			HttpServletResponse response, HttpServletRequest request) throws IOException {

		CommentDTO updateComm = CommentDTO.builder()
											.boardNo(commentDTO.getBoardNo())
											.commNo(commNo)
											.commLv(commentDTO.getCommLv())
											.commOr(commentDTO.getCommOr())
											.commGr(commentDTO.getCommGr())
											.commWriter(commentDTO.getCommWriter())
											.commContent(commentDTO.getCommContent())
											.commRegdate(commentDTO.getCommRegdate())
											.commDel(commentDTO.getCommDel())
											.build();
		
		commentService.updateComm(commentDTO);

		response.sendRedirect("/board/board/" + updateComm.getBoardNo() + "?pageNum=" + pageNum + "&amount=" + amount + "&searchCondition=" + searchCondition + "&searchKeyword=" + searchKeyword);
	}
	
	
	// 답글 작성
	@RequestMapping("/insertReply")
	public void insertReply(CommentDTO commentDTO,
			@RequestParam("pageNum") int pageNum, @RequestParam("amount") int amount,
			@RequestParam("searchCondition") String searchCondition, @RequestParam("searchKeyword") String searchKeyword,
			HttpServletResponse response, HttpServletRequest request) throws IOException {
		commentService.insertReply(commentDTO);
		
		response.sendRedirect("/board/board/" + commentDTO.getBoardNo() + "?pageNum=" + pageNum + "&amount=" + amount + "&searchCondition=" + searchCondition + "&searchKeyword=" + searchKeyword);
	}
	
}
