package com.example.demo.requests;

import java.time.LocalDate;

import com.example.demo.models.TicketType;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketRequest {

	@NotNull
	private long approverId;

	@NotNull
	private TicketType type;

	@NotNull
	private String description;

	@NotNull
	private LocalDate date;

}
