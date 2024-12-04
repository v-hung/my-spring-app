package com.example.demo.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class TimeSheet extends BaseModel {

	@Id
	@Column(nullable = false, unique = true)
	private String id = UUID.randomUUID().toString();

	private LocalDate date;

	private LocalTime startTime;

	private LocalTime endTime;

	private int workMinutes;

	@Column(name = "user_id")
	private int userId;

}
