package com.example.demo.requests;

import java.time.LocalTime;

import com.example.demo.annotations.LocalTimeFormat;
import com.example.demo.constant.TimesheetConst;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class WorkTimeCreateUpdateRequest {

	@NotBlank
	private String title;

	@LocalTimeFormat
	private LocalTime startTimeMorning = TimesheetConst.START_TIME_MORNING;

	@LocalTimeFormat
	private LocalTime endTimeMorning = TimesheetConst.END_TIME_MORNING;

	@LocalTimeFormat
	private LocalTime startTimeAfternoon = TimesheetConst.START_TIME_AFTERNOON;

	@LocalTimeFormat
	private LocalTime endTimeAfternoon = TimesheetConst.END_TIME_AFTERNOON;

	@LocalTimeFormat
	private int allowedLateMinutes = TimesheetConst.ALLOWED_LATE_MINUTES;

}
