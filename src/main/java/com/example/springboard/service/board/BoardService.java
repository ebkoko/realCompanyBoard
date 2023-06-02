package com.example.springboard.service.board;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestParam;

import com.example.springboard.dto.BoardDTO;
import com.example.springboard.dto.BoardFileDTO;
import com.example.springboard.dto.Criteria;

public interface BoardService {
	void insertBoard(BoardDTO boardDTO, List<BoardFileDTO> uploadFileList);
	
	List<BoardDTO> getBoardList(@RequestParam Map<String, String> paramMap, Criteria cri);
	
	void updateBoardCnt(int boardNo);
	
	BoardDTO getBoard(int boardNo);
	
	void deleteBoard(int boardNo);
	
	void updateBoard(BoardDTO boardDTO);
	
	List<BoardFileDTO> getBoardFileList(int boardNo);
	
	int getBoardTotalCnt(Map<String, String> paramMap);
	
	BoardFileDTO getBoardFile(BoardFileDTO boardFileDTO);
	
	BoardDTO pwCheck(BoardDTO boardDTO);
	
	List<BoardDTO> excelDown(@RequestParam Map<String, String> paramMap, Criteria cri);
	
	List<BoardDTO> getBoardListByCategory(@RequestParam int category);
	
//	List<BoardDTO> getBoardListByCategory(@RequestParam Map<String, String> paramMap, Criteria cri);
//	
//	int getBoardTotalCntByCategory(Map<String, String> paramMap);
}
