package com.example.demo.models;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import com.example.demo.annotations.JsonConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class Ticket extends Timestamp {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private long id;

	@JoinColumn(nullable = false)
	@ManyToOne
	private User creator;

	@JoinColumn(nullable = false)
	@ManyToOne
	private User approver;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TicketType type;

	@Column(columnDefinition = "TEXT")
	@Convert(converter = JsonConverter.class)
	private Map<String, Object> typeSpecificData;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TicketStatus status = TicketStatus.PENDING;

	@Column(nullable = false)
	private LocalDate date;

	@Column(nullable = false)
	private String description;

	private void writeObject(java.io.ObjectOutputStream stream) throws IOException {

		stream.defaultWriteObject();

	}

	private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {

		stream.defaultReadObject();

	}

}
