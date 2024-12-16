package com.example.demo.models;

import java.io.Serializable;
import java.time.LocalTime;

import com.example.demo.annotations.LocalTimeFormat;
import com.example.demo.constant.TimeSheetConst;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class WorkTime implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotBlank
	private String title;

	@NotNull
	@LocalTimeFormat
	private LocalTime startTimeMorning = TimeSheetConst.START_TIME_MORNING;

	@NotNull
	@LocalTimeFormat
	private LocalTime endTimeMorning = TimeSheetConst.END_TIME_MORNING;

	@NotNull
	@LocalTimeFormat
	private LocalTime startTimeAfternoon = TimeSheetConst.START_TIME_AFTERNOON;

	@NotNull
	@LocalTimeFormat
	private LocalTime endTimeAfternoon = TimeSheetConst.END_TIME_AFTERNOON;

	@NotNull
	@LocalTimeFormat
	private int allowedLateMinutes = TimeSheetConst.ALLOWED_LATE_MINUTES;

}
