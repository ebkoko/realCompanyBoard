package com.example.springboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
	private int cateNo;			// 카테고리 번호
	private String cateName;	// 카테고리 이름
}
