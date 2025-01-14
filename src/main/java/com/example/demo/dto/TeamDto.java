package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamDto {

	@NotNull
	private long id;

	@NotNull
	private String name;

	private String description;

	@NotNull
	private int totalMembers = 0;

	@NotNull
	private int completedProjects = 0;

	@NotNull
	private int activeProjects = 0;

	private UserDto manager;

	@NotNull
	@JsonManagedReference
	private List<UserDto> members = new ArrayList<>();

}
