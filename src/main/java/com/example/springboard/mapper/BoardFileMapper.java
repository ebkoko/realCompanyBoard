package com.example.springboard.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.data.repository.query.Param;

import com.example.springboard.dto.BoardDTO;
import com.example.springboard.dto.BoardFileDTO;

@Mapper
public interface BoardFileMapper {
	@Select("SELECT IFNULL(MAX(F.FILE_NO), 0) + 1"
			+ " FROM T_SB_FILE F"
			+ " WHERE F.BOARD_NO = #{boardNo}")
	int getMaxFileNo(int boardNo); 
	
	@Insert("INSERT INTO T_SB_FILE ("
			+ "BOARD_NO,"
			+ " FILE_NO,"
			+ " FILE_NAME,"
			+ " FILE_ORI_NAME,"
			+ " FILE_PATH,"
			+ " FILE_CATE"
			+ ") VALUES ("
			+ "#{boardNo},"
			+ " (SELECT IFNULL(MAX(F.FILE_NO), 0) + 1 FROM T_SB_FILE F WHERE F.BOARD_NO = #{boardNo}),"
			+ " #{fileName},"
			+ " #{fileOriName},"
			+ " #{filePath},"
			+ " #{fileCate}"
			+ ")") 
	void insertBoardFile(BoardFileDTO boardFile);
	
	@Select("SELECT * FROM T_SB_FILE WHERE BOARD_NO = #{boardNo}")
	List<BoardFileDTO> getBoardFileList(int boardNo);
	
	@Select("SELECT * FROM T_SB_FILE WHERE BOARD_NO = #{boardNo} AND FILE_NO = #{fileNo}")
	BoardFileDTO getBoardFile(BoardFileDTO boardFileDTO);
}
