package com.example.demo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.example.demo.annotations.LocalTimeFormat;

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

	@LocalTimeFormat
	private LocalTime startTime;

	@LocalTimeFormat
	private LocalTime endTime;

	private int workMinutes;

	@NotNull
	private UserDto user;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

}
