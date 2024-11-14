package com.example.demo.responses;

import lombok.Data;

@Data
public class RefreshResponse {

	private String token;

	private String refreshToken;

}
