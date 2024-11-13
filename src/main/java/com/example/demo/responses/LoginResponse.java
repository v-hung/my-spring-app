package com.example.demo.responses;

import com.example.demo.dto.UserDto;

import lombok.Data;

@Data
public class LoginResponse {

	private UserDto user;

	private String token;

	private String refreshToken;

}
