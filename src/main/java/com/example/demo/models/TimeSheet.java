package com.example.demo.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class TimeSheet extends BaseModel {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	private LocalDate date;

	private LocalTime startTime;

	private LocalTime endTime;

	private int workMinutes;

	@ManyToOne
	@JsonIgnoreProperties("timeSheets")
	private User user;

}
