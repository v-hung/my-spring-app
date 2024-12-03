package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.authorization.HasPermission;
import com.example.demo.models.Book;
import com.example.demo.repositories.BookRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

	private final BookRepository bookRepository;

	@HasPermission("BOOK_READ")
	@GetMapping("/")
	public List<Book> getAllBooks() {

		return bookRepository.findAll();

	}

}
