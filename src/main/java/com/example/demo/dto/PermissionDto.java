package com.example.demo.dto;

import com.example.demo.models.PermissionType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionDto {

	@NotNull
	private long id;

	@NotBlank
	@Enumerated(EnumType.STRING)
	private PermissionType name;
}
