package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.models.UserPosition;
import com.example.demo.models.UserStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

	@NotNull
	private Long id;

	@NotNull
	private String name;

	@NotNull
	private String email;

	@Enumerated(EnumType.STRING)
	private UserPosition position;

	@JsonIgnoreProperties({ "supervisor", "roles", "refreshTokens", "workTime", "team" })
	private UserDto supervisor;

	@JsonBackReference
	private TeamDto team;

	@NotNull
	@Enumerated(EnumType.STRING)
	private UserStatus status = UserStatus.ACTIVE;

	@NotNull
	private List<RoleDto> roles = new ArrayList<>();

	@NotNull
	private boolean isFirstLogin = true;

	@NotNull
	private int leaveHours = 0;

}
