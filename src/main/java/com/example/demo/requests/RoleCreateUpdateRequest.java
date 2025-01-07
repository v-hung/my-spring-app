package com.example.demo.requests;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.dto.PermissionDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleCreateUpdateRequest {

	@NotBlank
	private String name;

	private String description;

	private boolean admin = false;

	private int level = 1;

	private List<PermissionDto> permissions = new ArrayList<>();
}
