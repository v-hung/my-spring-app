package com.example.demo.dto;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWithPermissionDto {

	@NotNull
	private Long id;

	@NotNull
	private String name;

	@NotNull
	private String username;

	@NotNull
	private String email;

	@NotNull
	private Set<RoleWithPermissionDto> roles = new HashSet<>();

}
