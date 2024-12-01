package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ErrorResponse {

	private int status;

	private String message;

	private String timestamp = LocalDateTime.now().toString();

	private String error;

	private String path;

}
