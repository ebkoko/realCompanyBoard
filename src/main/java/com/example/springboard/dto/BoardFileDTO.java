package com.example.springboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardFileDTO {
	private int boardNo;		// 게시글 번호
	private int fileNo;			// 파일 번호
	private String fileName;	// 파일명
	private String fileOriName;	// 원본 파일명
	private String filePath;	// 파일 경로
	private String fileCate;	// 파일 종류(이미지, 문서, 압축파일)
	private String fileStatus;	// 파일 상태
	private String newFileName;	// 새 파일명
}
