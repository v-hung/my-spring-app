package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.models.Permission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleWithPermissionDto {

	@NotNull
	private long id;

	@NotBlank
	private String name;

	private String description;

	@NotNull
	private boolean admin = false;

	@NotNull
	private int level = 1;

	@NotNull
	private List<Permission> permissions = new ArrayList<>();

}
