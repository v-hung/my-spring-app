package com.example.demo.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
public class Ticket extends Timestamp {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private long id;

	@NotNull
	@ManyToOne
	private User creator;

	@NotNull
	@ManyToOne
	private User approver;

	@NotNull
	@Enumerated(EnumType.STRING)
	private TicketType type;

	@NotNull
	@Enumerated(EnumType.STRING)
	private TicketStatus status = TicketStatus.PENDING;

	@Column(nullable = false)
	private LocalDate date;

	@NotNull
	private String description;

}
