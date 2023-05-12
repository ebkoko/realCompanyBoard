package com.example.springboard.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.springboard.dto.BoardDTO;
import com.example.springboard.dto.Criteria;

@Mapper
public interface BoardMapper {
//	@SelectKey(
//			statementType=StatementType.PREPARED,
//			statement="SELECT MAX(B.BOARD_NO)+1 FROM T_SB_BOARD B",
//			keyProperty="boardNo",
//			before=true,
//			resultType=int.class
//			)
//	@Insert("INSERT INTO T_SB_BOARD ("
//			+ "BOARD_NO,"
//			+ "CATE_NO,"
//			+ "BOARD_TITLE,"
//			+ "BOARD_CONTENT,"
//			+ "BOARD_WRITER,"
//			+ "BOARD_REGDATE,"
//			+ "BOARD_CNT"
//			+ ") VALUES ("
//			+ "(SELECT IFNULL(MAX(A.BOARD_NO), 0) + 1 FROM T_SB_BOARD A),"
//			+ "#{cateNo},"
//			+ "#{boardTitle},"
//			+ "#{boardContent},"
//			+ "#{boardWriter},"
//			+ "NOW(),"
//			+ "0)") 
	void insertBoard(BoardDTO boardDTO);
	
//	@Select("SELECT * FROM T_SB_BOARD")
	List<BoardDTO> getBoardList(Map<String, Object> pMap);
	
//	@Update("UPDATE T_SB_BOARD"
//			+ " SET BOARD_CNT = BOARD_CNT + 1"
//			+ " WHERE BOARD_NO = #{boardNo}") 
	void updateBoardCnt(int boardNo);
	
//	@Select("SELECT * FROM T_SB_BOARD WHERE BOARD_NO = #{boardNo}")
	BoardDTO getBoard(int boardNo);
	
//	@Delete("DELETE FROM T_SB_BOARD WHERE BOARD_NO = #{boardNo}")
	void deleteBoard(int boardNo);
	
//	@Update("UPDATE T_SB_BOARD"
//			+ " SET BOARD_TITLE = #{boardTitle},"
//			+ " 	BOARD_CONTENT = #{boardContent}"
//			+ " WHERE BOARD_NO = #{boardNo}") 
	void updateBoard(BoardDTO boardDTO);
	
	int getBoardTotalCnt(Map<String, String> paramMap);
	
	BoardDTO pwCheck(BoardDTO boardDTO);
	
}
