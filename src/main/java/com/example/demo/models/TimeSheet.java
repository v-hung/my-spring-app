package com.example.demo.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class TimeSheet extends Timestamp {

	@Id
	@Column(nullable = false, unique = true)
	private String id = UUID.randomUUID().toString();

	@NotNull
	private LocalDate date;

	private LocalTime startTime;

	private LocalTime endTime;

	private int workMinutes;

	@ManyToOne
	@NotNull
	@JsonBackReference
	private User user;

}
