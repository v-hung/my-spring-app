package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

	List<Ticket> findByCreatorId(int id);

	List<Ticket> findByApproverId(int id);

}
