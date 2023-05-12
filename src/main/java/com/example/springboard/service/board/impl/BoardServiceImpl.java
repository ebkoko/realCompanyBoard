package com.example.springboard.service.board.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.springboard.dto.BoardDTO;
import com.example.springboard.dto.BoardFileDTO;
import com.example.springboard.dto.Criteria;
import com.example.springboard.mapper.BoardFileMapper;
import com.example.springboard.mapper.BoardMapper;
import com.example.springboard.service.board.BoardService;

@Service
public class BoardServiceImpl implements BoardService {
	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private BoardFileMapper boardFileMapper;
	
	@Override
	public void insertBoard(BoardDTO boardDTO, List<BoardFileDTO> uploadFileList) {
		boardMapper.insertBoard(boardDTO);
		
		for(BoardFileDTO boardFile : uploadFileList) {
			boardFile.setBoardNo(boardDTO.getBoardNo());
			
			int fileNo = boardFileMapper.getMaxFileNo(boardDTO.getBoardNo());
			boardFile.setFileNo(fileNo);
			
			boardFileMapper.insertBoardFile(boardFile);
		}
	}
	
	@Override
	public List<BoardDTO> getBoardList(@RequestParam Map<String, String> paramMap, Criteria cri) {
		Map<String, Object> pMap = new HashMap<String, Object>();
		
		pMap.put("boardSearch", paramMap);

		cri.setStartNum((cri.getPageNum() - 1) * cri.getAmount());
		pMap.put("cri", cri);
		
		return boardMapper.getBoardList(pMap);
	}
	
	@Override
	public void updateBoardCnt(int boardNo) {
		boardMapper.updateBoardCnt(boardNo);
	}
	
	@Override
	public BoardDTO getBoard(int boardNo) {
		return boardMapper.getBoard(boardNo);
	}
	
	@Override
	public void deleteBoard(int boardNo) {
		boardMapper.deleteBoard(boardNo);
	}
	
	@Override
	public void updateBoard(BoardDTO boardDTO) {
		boardMapper.updateBoard(boardDTO);
	}
	
	@Override
	public List<BoardFileDTO> getBoardFileList(int boardNo) {
//		BoardDTO board = BoardDTO.builder()
//								 .boardNo(boardNo)
//								 .build();
		
		return boardFileMapper.getBoardFileList(boardNo);
	}
	
	@Override
	public int getBoardTotalCnt(Map<String, String> paramMap) {		
		return boardMapper.getBoardTotalCnt(paramMap);
	}
	
	@Override
	public BoardFileDTO getBoardFile(BoardFileDTO boardFileDTO) {
		return boardFileMapper.getBoardFile(boardFileDTO);
	}
	
	@Override
	public BoardDTO pwCheck(BoardDTO boardDTO) {
		return boardMapper.pwCheck(boardDTO);
	}
}
