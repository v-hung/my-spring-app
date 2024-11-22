package com.example.demo.responses;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RefreshResponse {

	private String token;

	private String refreshToken;

}
