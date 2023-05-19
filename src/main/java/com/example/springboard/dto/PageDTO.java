package com.example.springboard.dto;

import lombok.Data;

@Data
public class PageDTO {
	private int startPage;
	private int endPage;
	private boolean prev, next;
	private int total;
	private Criteria cri;
	private int displayPageNum = 10;
	private int lastPage;
	
	public PageDTO(Criteria cri, int total) {
		this.cri = cri;
		this.total = total;
		
		this.endPage = (int)(Math.ceil(cri.getPageNum() / (double)displayPageNum) * displayPageNum);
		this.startPage = (this.endPage - this.displayPageNum) + 1;
		
		this.lastPage = (int)(Math.ceil((double)total / (double)cri.getAmount()));
		
		int realEnd = (int)(Math.ceil((total * 1.0) / (double)cri.getAmount()));
		
		if(realEnd < this.endPage) {
			this.endPage = realEnd;
		}
		
		this.prev = cri.getPageNum() > 1;
		//this.next = cri.getPageNum() < this.endPage;
		this.next = this.endPage * cri.getAmount() >= total ? false : true;
		
	}
}
