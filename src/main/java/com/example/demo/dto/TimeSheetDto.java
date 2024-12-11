package com.example.demo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeSheetDto {

	@NotNull
	private String id;

	@NotNull
	private LocalDate date;

	@Schema(type = "string", example = "14:30:00.311")
	private LocalTime startTime;

	@Schema(type = "string", example = "14:30:00.311")
	private LocalTime endTime;

	private int workMinutes;

	@NotNull
	private UserDto user;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

}
