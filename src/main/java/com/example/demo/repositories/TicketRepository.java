package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

}
