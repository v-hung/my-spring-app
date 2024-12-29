package com.example.demo.dto;

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

}
