package com.example.springboard.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.springboard.common.FileUtils;
import com.example.springboard.dto.BoardDTO;
import com.example.springboard.dto.BoardFileDTO;
import com.example.springboard.dto.CommentDTO;
import com.example.springboard.dto.Criteria;
import com.example.springboard.dto.PageDTO;
import com.example.springboard.dto.ResponseDTO;
import com.example.springboard.dto.UserDTO;
import com.example.springboard.service.board.BoardService;
import com.example.springboard.service.comment.CommentService;

@RestController
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private CommentService commentService;
	
	// 게시판 목록
	@RequestMapping("/boardList")
	public ModelAndView getBoardList(@RequestParam Map<String, String> paramMap, Criteria cri) {
		List<BoardDTO> boardList = boardService.getBoardList(paramMap, cri);
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("board/getBoardList.html");
		mv.addObject("getBoardList", boardList);
		
		if(paramMap.get("searchCondition") != null && !paramMap.get("searchCondition").equals("")) {
			mv.addObject("searchCondition", paramMap.get("searchCondition"));
		}
		
		if(paramMap.get("searchKeyword") != null && !paramMap.get("searchKeyword").equals("")) {
			mv.addObject("searchKeyword", paramMap.get("searchKeyword"));
		}
		
		int total = boardService.getBoardTotalCnt(paramMap);
		mv.addObject("pageDTO", new PageDTO(cri, total)); 
				
		return mv;
	}
	
	// 게시글 등록
	@GetMapping("/insertBoard")
	public ModelAndView insertBoardView(HttpSession session) throws IOException {
		UserDTO loginUser = (UserDTO)session.getAttribute("loginUser");
		
		ModelAndView mv = new ModelAndView();
		
		if(loginUser == null) {
			mv.setViewName("board/insertBoardN.html");
			
			return mv;
		} else {
			mv.setViewName("board/insertBoard.html");	
			mv.addObject("loginUser", loginUser);
			
			return mv;			
		}
		
	}
	
	@PostMapping("/board")
	public void insertBoard(BoardDTO boardDTO, MultipartFile[] uploadFiles,
			HttpServletResponse response, HttpServletRequest request) throws IOException {
		// DB에 입력될 파일 정보 리스트
		List<BoardFileDTO> uploadFileList = new ArrayList<BoardFileDTO>();
		
		// 업로드할 파일이 있는 경우
		if(uploadFiles.length > 0) {
			String attachPath = request.getSession().getServletContext().getRealPath("/") + "/upload/";
			
			File directory = new File(attachPath);
			
			// 업로드한 파일 경로가 없는 경우
			if(!directory.exists()) {
				directory.mkdir();
			}
			
			// multipartFile 형식의 데이터를 DB 테이블에 맞는 구조로 변경
			for(int i = 0; i < uploadFiles.length; i++) {
				MultipartFile file = uploadFiles[i];
				
				if(!file.getOriginalFilename().equals("") &&
						file.getOriginalFilename() != null) {
					BoardFileDTO boardFile = new BoardFileDTO();
					
					boardFile = FileUtils.parseFileInfo(file, attachPath);
					
					uploadFileList.add(boardFile);
				}
			}
		}
		
		
		boardService.insertBoard(boardDTO, uploadFileList);
		
		response.sendRedirect("/board/boardList");
	}
	
	// 조회수 증가
	@GetMapping("/updateBoardCnt/{boardNo}")
	public void updateBoardCnt(@PathVariable int boardNo, HttpServletResponse response) throws IOException {
		boardService.updateBoardCnt(boardNo);
		
		response.sendRedirect("/board/board/" + boardNo);
	}
	
	// 게시글 상세
	@GetMapping("/board/{boardNo}")
	public ModelAndView getBoard(@PathVariable int boardNo, HttpSession session, CommentDTO commentDTO) {
		UserDTO loginUser = (UserDTO)session.getAttribute("loginUser");
				
		BoardDTO board = boardService.getBoard(boardNo);
		
		BoardDTO boardDTO = BoardDTO.builder()
									.boardNo(board.getBoardNo())
									.cateNo(board.getCateNo())
									.boardTitle(board.getBoardTitle())
									.boardContent(board.getBoardContent())
									.boardWriter(board.getBoardWriter())
									.boardPw(board.getBoardPw())
									.boardRegdate(
											board.getBoardRegdate() == null ?
											null :
											board.getBoardRegdate().toString())
									.boardCnt(board.getBoardCnt())
									.build();
		
		List<BoardFileDTO> boardFileList = boardService.getBoardFileList(boardDTO.getBoardNo());
		
		List<BoardFileDTO> boardFileDTOList = new ArrayList<BoardFileDTO>();
		
		for(BoardFileDTO boardFile : boardFileList) {
			BoardFileDTO boardFileDTO = BoardFileDTO.builder()
													.boardNo(boardNo)
													.fileNo(boardNo)
													.fileNo(boardFile.getFileNo())
													.fileName(boardFile.getFileName())
													.fileOriName(boardFile.getFileOriName())
													.filePath(boardFile.getFilePath())
													.fileCate(boardFile.getFileCate())
													.build();
			
			boardFileDTOList.add(boardFileDTO);
		}
		
		ModelAndView mv = new ModelAndView();
		
		if(loginUser == null) {
			mv.setViewName("board/getBoardN.html");			
			mv.addObject("getBoard", boardDTO);
			mv.addObject("boardFileList", boardFileDTOList);
			
			// 댓글
			List<CommentDTO> comment = commentService.getComment(commentDTO);
			
			mv.addObject("comment", comment);
			
			return mv;
		
		} else {
			mv.setViewName("board/getBoard.html");	
			mv.addObject("loginUser", loginUser);
			mv.addObject("getBoard", boardDTO);
			mv.addObject("boardFileList", boardFileDTOList);
			
			// 댓글
			List<CommentDTO> comment = commentService.getComment(commentDTO);
			
			mv.addObject("comment", comment);
			
			return mv;		
		}
		
	}
	
	// 게시글 삭제
	@DeleteMapping("/board")
	public void deleteBoard(@RequestParam("boardNo") int boardNo) {
		boardService.deleteBoard(boardNo);
	}
	
	// 게시글 수정
	@GetMapping("/updateBoard/{boardNo}")
	public ModelAndView updateBoardView(@PathVariable("boardNo") int boardNo) {
		BoardDTO board = boardService.getBoard(boardNo);
		
		BoardDTO boardDTO = BoardDTO.builder()
									.boardNo(board.getBoardNo())
									.cateNo(board.getCateNo())
									.boardTitle(board.getBoardTitle())
									.boardContent(board.getBoardContent())
									.boardWriter(board.getBoardWriter())
									.boardPw(board.getBoardPw())
									.build();
		
		List<BoardFileDTO> boardFileList = boardService.getBoardFileList(boardDTO.getBoardNo());
		
		List<BoardFileDTO> boardFileDTOList = new ArrayList<BoardFileDTO>();
		
		for(BoardFileDTO boardFile : boardFileList) {
			BoardFileDTO boardFileDTO = BoardFileDTO.builder()
													.boardNo(boardNo)
													.fileNo(boardNo)
													.fileNo(boardFile.getFileNo())
													.fileName(boardFile.getFileName())
													.fileOriName(boardFile.getFileOriName())
													.filePath(boardFile.getFilePath())
													.fileCate(boardFile.getFileCate())
													.build();
			
			boardFileDTOList.add(boardFileDTO);
		}
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("board/updateBoard.html");
		mv.addObject("getBoard", boardDTO);
		mv.addObject("boardFileList", boardFileDTOList);
		
		return mv;
	}
	
	
	@PostMapping("/pwCheck")
	public ResponseEntity<?> pwCheck(BoardDTO boardDTO) {
		ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
		Map<String, String> returnMap = new HashMap<String, String>();
		
		try {
			BoardDTO checkedBoard = boardService.pwCheck(boardDTO);
			
			
			// 게시글 비번 확인
			if(checkedBoard == null) {
				returnMap.put("msg", "pwFail");
			} else {
				// 게시글 번호 확인
				if(checkedBoard.getBoardNo() != boardDTO.getBoardNo()) {
					returnMap.put("msg", "boardNoDiff");
				} else {
					BoardDTO okBoard = BoardDTO.builder()
												.boardNo(checkedBoard.getBoardNo())
												.cateNo(checkedBoard.getCateNo())
												.boardTitle(checkedBoard.getBoardTitle())
												.boardWriter(checkedBoard.getBoardWriter())
												.boardPw(checkedBoard.getBoardPw())
												.boardRegdate(checkedBoard.getBoardRegdate())
												.boardCnt(checkedBoard.getBoardCnt())
												.boardContent(checkedBoard.getBoardContent())
												.build();
												
					returnMap.put("msg", "boardPwSuccess");
				}
				
			}			
			
			responseDTO.setItem(returnMap);
			
			return ResponseEntity.ok().body(responseDTO);
		} catch(Exception e) {
			responseDTO.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(responseDTO);
		}
		
		
	}
	
	
	@PostMapping("/updateBoard")
	public void updateBoard(BoardDTO boardDTO,
			HttpServletResponse response, HttpServletRequest request) throws IOException {
		BoardDTO board = BoardDTO.builder()
								 .boardNo(boardDTO.getBoardNo())
								 .cateNo(boardDTO.getCateNo())
								 .boardTitle(boardDTO.getBoardTitle())
								 .boardContent(boardDTO.getBoardContent())
								 .boardWriter(boardDTO.getBoardWriter())
								 .boardPw(boardDTO.getBoardPw())
								 .build();
		
		boardService.updateBoard(boardDTO);
		
		response.sendRedirect("board/" + board.getBoardNo());
	}
	
	// 파일 다운
	@RequestMapping("/fileDown")
	@ResponseBody
	public ResponseEntity<Resource> fileDown(BoardFileDTO boardFileDTO) {
		BoardFileDTO boardFile = boardService.getBoardFile(boardFileDTO);
		
		Resource resource = new FileSystemResource(
				boardFile.getFilePath() + boardFile.getFileName());
		
		String resourceName = resource.getFilename();
		
		HttpHeaders header = new HttpHeaders();
		
		try {
			header.add("Content-Disposition", "attachment; filename="
					+ new String(resourceName.getBytes("UTF-8"), "ISO-8859-1"));
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
	}
	
	// 목록 엑셀 다운로드
//	@GetMapping("/excelDown")
//	public void excelDownload(HttpServletResponse response) throws IOException {
//		XSSFWorkbook wb = new XSSFWorkbook();
//		Sheet sheet = wb.createSheet("mysheet이름");
//		Row row = null;
//		Cell cell = null;
//				
//		// row(행) 생성
//		row = sheet.createRow(0); // 0번째 행
//		cell = row.createCell(0);
//		cell.setCellValue("0");
//		cell = row.createCell(1);
//		cell.setCellValue("가가가");
//		cell = row.createCell(2);
//		cell.setCellValue("나나나");
//		
//		row = sheet.createRow(1); // 1번째 행
//		cell = row.createCell(0);
//		cell.setCellValue("1");
//		cell = row.createCell(1);
//		cell.setCellValue("AAA");
//		cell = row.createCell(2);
//		cell.setCellValue("BBB");
//		
//		row = sheet.createRow(2); // 2번째 행
//		cell = row.createCell(0);
//		cell.setCellValue("2");
//		cell = row.createCell(1);
//		cell.setCellValue("aaa");
//		cell = row.createCell(2);
//		cell.setCellValue("bbb");
//		
//		// 컨텐츠 타입과 파일명 지정
//		response.setContentType("ms-vnd/excel");
//		response.setHeader("Content-Disposition", "attachment;filename=test01.xlsx"); // 파일이름 지정
//		//response OutputStream에 엑셀 작성
//		wb.write(response.getOutputStream());		
//		
//	}
	
	@GetMapping("/excelDown")
	public void excelDownload(BoardDTO boardDTO, HttpServletResponse response) throws IOException {
		XSSFWorkbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("게시판 목록");
		Row row = null;
		Cell cell = null;
		
		List<BoardDTO> boardListDown = boardService.excelDown(boardDTO);
		
		// 첫 행 열 이름 표기
		int cellCount = 0;
		row = sheet.createRow(0); // 0번째 행
		cell = row.createCell(cellCount++);
		cell.setCellValue("글 번호");
		cell = row.createCell(cellCount++);
		cell.setCellValue("글 종류");
		cell = row.createCell(cellCount++);
		cell.setCellValue("글 제목");
		cell = row.createCell(cellCount++);
		cell.setCellValue("작성자");
		cell = row.createCell(cellCount++);
		cell.setCellValue("글 비밀번호(비회원시)");
		cell = row.createCell(cellCount++);
		cell.setCellValue("글 내용");
		cell = row.createCell(cellCount++);
		cell.setCellValue("첨부파일 개수");
		cell = row.createCell(cellCount++);
		cell.setCellValue("작성일");
		cell = row.createCell(cellCount++);
		cell.setCellValue("조회수");
		
		for(int i = 0; i < boardListDown.size(); i++) {
			row = sheet.createRow(i+1);
			cellCount = 0;
			cell = row.createCell(cellCount++);
			cell.setCellValue(boardListDown.get(i).getBoardNo());
			cell = row.createCell(cellCount++);
			cell.setCellValue(boardListDown.get(i).getCateName());
			cell = row.createCell(cellCount++);
			cell.setCellValue(boardListDown.get(i).getBoardTitle());
			cell = row.createCell(cellCount++);
			cell.setCellValue(boardListDown.get(i).getBoardWriter());
			cell = row.createCell(cellCount++);
			cell.setCellValue(boardListDown.get(i).getBoardPw());
			cell = row.createCell(cellCount++);
			cell.setCellValue(boardListDown.get(i).getBoardContent());
			cell = row.createCell(cellCount++);
			cell.setCellValue(boardListDown.get(i).getFileNoCnt());
			cell = row.createCell(cellCount++);
			cell.setCellValue(boardListDown.get(i).getBoardRegdate());
			cell = row.createCell(cellCount++);
			cell.setCellValue(boardListDown.get(i).getBoardCnt());
		}
		
		// 컨텐츠 타입과 파일명 지정
		response.setContentType("ms-vnd/excel");
		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("게시판_목록", "UTF-8") + ".xlsx");
		// response OutputStream에 엑셀 작성
		wb.write(response.getOutputStream());
		wb.close();
	}
	
}
