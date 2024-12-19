package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDto {

	@NotNull
	private int id;

	@NotBlank
	private String name;

	private String description;

}
