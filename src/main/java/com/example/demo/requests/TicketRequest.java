package com.example.demo.requests;

import java.time.LocalDate;
import java.util.Map;

import com.example.demo.annotations.JsonConverter;
import com.example.demo.models.TicketType;

import jakarta.persistence.Convert;
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

	@Convert(converter = JsonConverter.class)
	private Map<String, Object> typeSpecificData;

}
