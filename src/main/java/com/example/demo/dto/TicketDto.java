package com.example.demo.dto;

import java.time.LocalDateTime;

import com.example.demo.models.TicketStatus;
import com.example.demo.models.TicketType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketDto {

	@NotNull
	private long id;

	@NotNull
	private UserDto creator;

	@NotNull
	private UserDto approver;

	@NotNull
	@Enumerated(EnumType.STRING)
	private TicketType type;

	@NotNull
	@Enumerated(EnumType.STRING)
	private TicketStatus status = TicketStatus.PENDING;

	@NotNull
	private String description;

	@NotNull
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	@NotNull
	private UserDto createdBy;

	private UserDto lastModifiedBy;

}
