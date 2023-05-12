package com.example.springboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {
	private int boardNo;		// 게시글 번호
	private int commNo;			// 댓글 번호
	private int commLv;			// 댓글 레벨(댓글:0, 대댓글:1)
	private int commOr;			// 댓글 순서(그룹 안에서의 댓글 순서)
	private int commGr;			// 댓글 그룹(댓글과 대댓글이 하나의 그룹)
	private String commWriter;	// 댓글 작성자
	private String commContent;	// 댓글 내용
	private String commRegdate;	// 댓글 작성일
}
