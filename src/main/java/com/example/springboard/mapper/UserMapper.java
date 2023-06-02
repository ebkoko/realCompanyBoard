package com.example.springboard.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.springboard.dto.UserDTO;

@Mapper
public interface UserMapper {
//	@Select("SELECT * FROM T_SB_USER WHERE USER_ID = #{userId}")
	UserDTO idCheck(UserDTO userDTO);
}
