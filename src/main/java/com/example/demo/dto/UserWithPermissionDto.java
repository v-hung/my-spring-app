package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;

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
	private List<RoleWithPermissionDto> roles = new ArrayList<>();

}
