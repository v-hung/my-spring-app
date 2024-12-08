package com.example.demo.dto;

import java.time.LocalDate;
import java.time.LocalTime;
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

	private LocalTime startTime;

	private LocalTime endTime;

	private int workMinutes;

	@NotNull
	private UserDto user;

}
