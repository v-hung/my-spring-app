package com.example.demo.requests;

import com.example.demo.models.UserPosition;
import com.example.demo.models.UserStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UserSearchResponse {

	private String name;

	private String email;

	@Enumerated(EnumType.STRING)
	private UserPosition position;

	@Enumerated(EnumType.STRING)
	private UserStatus status;

}
