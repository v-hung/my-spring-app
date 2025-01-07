package com.example.demo.dto;

import java.time.LocalTime;

import com.example.demo.annotations.LocalTimeFormat;
import com.example.demo.constant.TimesheetConst;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkTimeDto {

	@NotNull
	private long id;

	@NotBlank
	private String title;

	@NotNull
	@LocalTimeFormat
	private LocalTime startTimeMorning = TimesheetConst.START_TIME_MORNING;

	@NotNull
	@LocalTimeFormat
	private LocalTime endTimeMorning = TimesheetConst.END_TIME_MORNING;

	@NotNull
	@LocalTimeFormat
	private LocalTime startTimeAfternoon = TimesheetConst.START_TIME_AFTERNOON;

	@NotNull
	@LocalTimeFormat
	private LocalTime endTimeAfternoon = TimesheetConst.END_TIME_AFTERNOON;

	@NotNull
	@LocalTimeFormat
	private int allowedLateMinutes = TimesheetConst.ALLOWED_LATE_MINUTES;

}
