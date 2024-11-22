package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ErrorResponse {

	private int status;

	private String message;

	private String timestamp = LocalDateTime.now().toString();

	private String error;

	private String path;

}
