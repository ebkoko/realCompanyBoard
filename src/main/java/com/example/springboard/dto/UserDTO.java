package com.example.springboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
	private String userId;		// 회원 아이디
	private String userPw;		// 회원 비밀번호
	private String userRegdate;	// 회원 가입일
	private String userRole;	// 회원 구분(일반회원: ROLE_USER, 관리자: ROLE_ADMIN)
}
