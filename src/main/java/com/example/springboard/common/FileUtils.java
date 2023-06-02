package com.example.springboard.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.example.springboard.dto.BoardFileDTO;

public class FileUtils {
	public static BoardFileDTO parseFileInfo(MultipartFile file, String attachPath) throws IOException {
		BoardFileDTO boardFile = new BoardFileDTO();
		
		String boardOriginFileNm = file.getOriginalFilename();
		
		// 같은 파일명을 업로드했을 때 덮어써지지 않게 하기위한 실제 업로드되는 파일명 설정
		SimpleDateFormat formmater = new SimpleDateFormat("yyyyMMddHHmmss");
		Date nowDate = new Date();
		String nowDateStr = formmater.format(nowDate);
		
		String boardFileNm = nowDateStr + "_" + boardOriginFileNm;
		
		String boardFilePath = attachPath;
		
		// 파일 형태 검사
		File checkFile = new File(boardOriginFileNm);
		
		String type = Files.probeContentType(checkFile.toPath());
		
		if(type != null) {
			if(type.startsWith("image")) {
				boardFile.setFileCate("img");
			} else if(type.startsWith("application")) {
				if(type.endsWith("zip")) {
					boardFile.setFileCate("zip");
				} else {
					boardFile.setFileCate("document");			
				}
			}
		} else {
			boardFile.setFileCate("etc");
		}
		
		boardFile.setFileName(boardFileNm);
		boardFile.setFileOriName(boardOriginFileNm);
		boardFile.setFilePath(boardFilePath);
		
		// 실제 파일 업로드
		File uploadFile = new File(attachPath + boardFileNm);
		
		file.transferTo(uploadFile);
		
		return boardFile;
	}
}
