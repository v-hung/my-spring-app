package com.example.demo.requests;

import com.example.demo.models.TicketType;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketRequest {

	@NotNull
	private int approverId;

	@NotNull
	private TicketType type;

	@NotNull
	private String description;

}
