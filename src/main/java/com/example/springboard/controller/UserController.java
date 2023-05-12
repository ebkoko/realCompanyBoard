package com.example.springboard.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.springboard.dto.ResponseDTO;
import com.example.springboard.dto.UserDTO;
import com.example.springboard.service.user.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	@GetMapping("/login")
	public ModelAndView loginView() {
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("/user/login.html");
		return mv;
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(UserDTO userDTO, HttpSession session) {
		ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
		Map<String, String> returnMap = new HashMap<String, String>();
		
		try {						
			UserDTO checkedUser = userService.idCheck(userDTO);
			System.out.println(checkedUser);
			
			// 아이디 확인
			if(checkedUser == null) {
				returnMap.put("msg", "idFail");
			} else {
				// 비밀번호 확인
				if(!checkedUser.getUserPw().equals(userDTO.getUserPw())) {
					returnMap.put("msg", "pwFail");
				} else {
					UserDTO loginUser = UserDTO.builder()
												.userId(checkedUser.getUserId())
												.userPw(checkedUser.getUserPw())
												.userRegdate(checkedUser.getUserRegdate())
												.userRole(checkedUser.getUserRole())
												.build();
					
					// 로그인
					session.setAttribute("loginUser", loginUser);
					returnMap.put("msg", "loginSuccess");
				}
			}
			
			responseDTO.setItem(returnMap);
			
			return ResponseEntity.ok().body(responseDTO);
		} catch(Exception e) {
			responseDTO.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	@RequestMapping("/logout")
	public void logout(HttpSession session, HttpServletResponse response) throws IOException {
		session.invalidate();
		
		response.sendRedirect("/");
	}
}
