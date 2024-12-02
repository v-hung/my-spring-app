package com.example.demo.models;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Set;

import com.example.demo.constant.TimeSheetConst;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
	private Long id;

	private String title;

	private LocalTime startTimeMorning = TimeSheetConst.START_TIME_MORNING;

	private LocalTime endTimeMorning = TimeSheetConst.END_TIME_MORNING;

	private LocalTime startTimeAfternoon = TimeSheetConst.START_TIME_AFTERNOON;

	private LocalTime endTimeAfternoon = TimeSheetConst.END_TIME_AFTERNOON;

	@OneToMany
	private Set<User> user;
}
