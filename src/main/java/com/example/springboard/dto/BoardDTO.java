package com.example.springboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDTO {
	private int boardNo;			// 게시글 번호
	private int cateNo;				// 카테고리 번호
	private String boardTitle;		// 게시글 제목
	private String boardContent;	// 게시글 내용
	private String boardWriter;		// 게시글 작성자
	private String boardPw;			// 게시글 비밀번호
	private String boardRegdate;	// 게시글 작성일
	private int boardCnt;			// 게시글 조회수
	private String searchCondition;	// 검색조건
	private String searchKeyword;	// 검색키워드
	private int fileNoCnt;			// 첨부파일 갯수
	private String userRole;		// 유저롤
	private String cateName;
	private String fileOriName;
}
