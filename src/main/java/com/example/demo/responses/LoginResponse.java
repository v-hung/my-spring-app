package com.example.demo.responses;

import com.example.demo.dto.UserWithPermissionDto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginResponse {

	private UserWithPermissionDto user;

	private String token;

	private String refreshToken;

}
