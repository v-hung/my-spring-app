package com.example.demo.configurations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.dto.ErrorResponse;
import com.example.demo.exception.BusinessException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex,
		HttpServletRequest request) {

		ErrorResponse errorResponse = new ErrorResponse()
			.setStatus(HttpStatus.NOT_FOUND.value())
			.setMessage("User not found")
			.setPath(request.getRequestURI())
			.setError(HttpStatus.NOT_FOUND.getReasonPhrase());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {

		ErrorResponse errorResponse = new ErrorResponse()
			.setStatus(ex.getStatus().value())
			.setMessage(ex.getMessage())
			.setPath(request.getRequestURI())
			.setError(ex.getStatus().getReasonPhrase());

		return ResponseEntity.status(ex.getStatus()).body(errorResponse);

	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {

		ErrorResponse errorResponse = new ErrorResponse()
			.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
			.setMessage(ex.getMessage())
			.setPath(request.getRequestURI())
			.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);

	}

}
