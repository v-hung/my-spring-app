package com.example.demo.models;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	@Nonnull
	private long id;

	@Nonnull
	private int creatorId;

	@Nonnull
	private int approverId;

	@Nonnull
	private TicketType ticketType;

	@Nonnull
	private String description;

}
