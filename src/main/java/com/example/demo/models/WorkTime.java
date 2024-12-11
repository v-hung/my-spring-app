package com.example.demo.models;

import java.io.Serializable;
import java.time.LocalTime;

import com.example.demo.constant.TimeSheetConst;

import io.swagger.v3.oas.annotations.media.Schema;
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
	@Schema(type = "string", example = "14:30:00.311")
	private LocalTime startTimeMorning = TimeSheetConst.START_TIME_MORNING;

	@NotNull
	@Schema(type = "string", example = "14:30:00.311")
	private LocalTime endTimeMorning = TimeSheetConst.END_TIME_MORNING;

	@NotNull
	@Schema(type = "string", example = "14:30:00.311")
	private LocalTime startTimeAfternoon = TimeSheetConst.START_TIME_AFTERNOON;

	@NotNull
	@Schema(type = "string", example = "14:30:00.311")
	private LocalTime endTimeAfternoon = TimeSheetConst.END_TIME_AFTERNOON;

	@NotNull
	@Schema(type = "string", example = "14:30:00.311")
	private int allowedLateMinutes = TimeSheetConst.ALLOWED_LATE_MINUTES;

}
