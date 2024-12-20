package com.example.demo.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Ticket;
import com.example.demo.models.TicketType;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

	List<Ticket> findByCreatorId(long id);

	List<Ticket> findByApproverId(long id);

	Optional<Ticket> findByCreatorIdAndTypeAndDate(long id, TicketType type, LocalDate date);

	Optional<Ticket> findByIdAndCreatorId(long id, long creatorId);

}
