package com.example.demo.services;

import java.util.Comparator;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.BusinessException;
import com.example.demo.models.Role;
import com.example.demo.models.Ticket;
import com.example.demo.models.TicketStatus;
import com.example.demo.models.User;
import com.example.demo.repositories.TicketRepository;
import com.example.demo.requests.TicketRequest;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TicketService {

	private final TicketRepository ticketRepository;

	private final ApprovalService approvalService;

	@Transactional
	public Ticket createTicket(TicketRequest ticketRequest, User user) {

		int currentLevel = user.getRoles().stream().map(Role::getLevel).max(Comparator.naturalOrder()).orElse(1);

		boolean approverIsValid;

		switch (ticketRequest.getType()) {

		case TIMESHEET_ADJUSTMENT:

			approverIsValid = approvalService.canApproveTimesheetAdjustment(ticketRequest.getApproverId(),
				currentLevel);
			break;

		case LEAVE_REQUEST:

			approverIsValid = approvalService.canApproveTimesheetAdjustment(ticketRequest.getApproverId(),
				currentLevel);
			break;

		default:
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Unsupported ticket type.");

		}

		if (!approverIsValid) {

			throw new BusinessException(HttpStatus.BAD_REQUEST, "Approver does not have the required permissions.");

		}

		Ticket ticket = new Ticket()
			.setCreator(user)
			.setApprover(new User().setId(ticketRequest.getApproverId()))
			.setType(ticketRequest.getType())
			.setStatus(TicketStatus.PENDING)
			.setDescription(ticketRequest.getDescription());

		ticketRepository.save(ticket);

		return ticket;

	}

	@Transactional(readOnly = true)
	public List<Ticket> getTicketsCreator(User user) {

		return ticketRepository.findByCreatorId(user.getId());

	}

	@Transactional(readOnly = true)
	public List<Ticket> getTicketsApprover(User user) {

		return ticketRepository.findByApproverId(user.getId());

	}
}
