package com.example.demo.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.TicketDto;
import com.example.demo.models.Ticket;
import com.example.demo.models.User;
import com.example.demo.requests.TicketRequest;
import com.example.demo.services.AuthenticationService;
import com.example.demo.services.TicketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

	private final TicketService ticketService;

	private final AuthenticationService authenticationService;

	private final ModelMapper modelMapper;

	@GetMapping("")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<TicketDto>> getTicketsCreator() {

		User currentUser = authenticationService.getCurrentUser();

		List<TicketDto> tickets = ticketService.getTicketsCreator(currentUser);

		return ResponseEntity.ok(tickets);

	}

	@GetMapping("/approver")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<TicketDto>> getTicketsApprover() {

		User currentUser = authenticationService.getCurrentUser();

		List<TicketDto> tickets = ticketService.getTicketsApprover(currentUser);

		return ResponseEntity.ok(tickets);

	}

	@PostMapping("")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<TicketDto> createTicket(@RequestBody TicketRequest ticketRequest) {

		User currentUser = authenticationService.getCurrentUser();

		Ticket ticket = ticketService.createTicket(ticketRequest, currentUser);

		return ResponseEntity.ok(modelMapper.map(ticket, TicketDto.class));

	}

	@GetMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<TicketDto> getTicket(@PathVariable Long id) {

		Ticket ticket = ticketService.getTicketByCurrentUser(id);

		return ResponseEntity.ok(modelMapper.map(ticket, TicketDto.class));

	}

	@PostMapping("/{id}/cancel")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<TicketDto> cancelTicket(@PathVariable Long id) {

		Ticket ticket = ticketService.cancelTicket(id);

		return ResponseEntity.ok(modelMapper.map(ticket, TicketDto.class));

	}

	@PostMapping("/{id}/approval")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<TicketDto> approvalTicket(@PathVariable Long id) {

		Ticket ticket = ticketService.approvalTicket(id);

		return ResponseEntity.ok(modelMapper.map(ticket, TicketDto.class));

	}

}
