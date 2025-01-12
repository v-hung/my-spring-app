package com.example.demo.responses;

import java.util.List;

import org.springframework.data.domain.Page;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageResponse<T> {

	@NotNull
	private List<T> content;

	@NotNull
	private int pageNumber;

	@NotNull
	private int pageSize;

	@NotNull
	private long totalElements;

	@NotNull
	private int totalPages;

	@NotNull
	private boolean first;

	@NotNull
	private boolean last;

	@NotNull
	private boolean empty;

	public PageResponse(Page<T> page) {

		this.content = page.getContent();
		this.pageNumber = page.getNumber();
		this.pageSize = page.getSize();
		this.totalElements = page.getTotalElements();
		this.totalPages = page.getTotalPages();
		this.first = page.isFirst();
		this.last = page.isLast();
		this.empty = page.isEmpty();

	}
}
