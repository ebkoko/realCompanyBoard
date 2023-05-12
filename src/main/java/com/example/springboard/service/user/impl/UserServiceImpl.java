package com.example.springboard.service.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboard.dto.UserDTO;
import com.example.springboard.mapper.UserMapper;
import com.example.springboard.service.user.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;
	
	@Override
	public UserDTO idCheck(UserDTO userDTO) {
		return userMapper.idCheck(userDTO);
	}
}
