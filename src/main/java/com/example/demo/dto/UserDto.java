package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.models.UserPosition;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

	@Column(nullable = false)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserPosition position;

	@Column(nullable = false)
	private Long supervisorId;

	@Column(nullable = false)
	private List<RoleDto> roles = new ArrayList<>();
}
