package com.example.demo.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.TicketDto;
import com.example.demo.exception.BusinessException;
import com.example.demo.models.Ticket;
import com.example.demo.models.TicketStatus;
import com.example.demo.models.User;
import com.example.demo.repositories.TicketRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.requests.TicketRequest;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TicketService {

	private final TicketRepository ticketRepository;

	private final ApprovalService approvalService;

	private final UserRepository userRepository;

	private final AuthenticationService authenticationService;

	@Transactional
	public Ticket createTicket(TicketRequest ticketRequest, User user) {

		boolean approverIsValid;

		switch (ticketRequest.getType()) {

		case TIMESHEET_ADJUSTMENT:

			approverIsValid = approvalService.canApproveTimesheetAdjustment(ticketRequest.getApproverId(),
				user.getCurrentLevel());
			break;

		case LEAVE_REQUEST:

			approverIsValid = approvalService.canApproveTimesheetAdjustment(ticketRequest.getApproverId(),
				user.getCurrentLevel());
			break;

		default:
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Unsupported ticket type.");

		}

		if (!approverIsValid) {

			throw new BusinessException(HttpStatus.BAD_REQUEST, "Approver does not have the required permissions.");

		}

		if (ticketRepository
			.findByCreatorIdAndTypeAndDate(user.getId(), ticketRequest.getType(), ticketRequest.getDate())
			.isPresent()) {

			throw new BusinessException(HttpStatus.CONFLICT, "Ticket already exits.");

		}

		User approverUser = userRepository.findById(ticketRequest.getApproverId())
			.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Approver user is not found"));

		Ticket ticket = new Ticket()
			.setCreator(user)
			.setApprover(approverUser)
			.setType(ticketRequest.getType())
			.setStatus(TicketStatus.PENDING)
			.setTypeSpecificData(ticketRequest.getTypeSpecificData())
			.setDate(ticketRequest.getDate())
			.setDescription(ticketRequest.getDescription());

		ticketRepository.save(ticket);

		return ticket;

	}

	@Transactional(readOnly = true)
	public List<TicketDto> getTicketsCreator(User user) {

		return ticketRepository.findByCreatorId(user.getId());

	}

	@Transactional(readOnly = true)
	public List<TicketDto> getTicketsApprover(User user) {

		return ticketRepository.findByApproverId(user.getId());

	}

	@Transactional(readOnly = true)
	public Ticket getTicketByCurrentUser(long id) {

		return findTicketByCurrentUser(id);

	}

	@Transactional
	public Ticket cancelTicket(long id) {

		Ticket ticket = findTicketByCurrentUser(id);

		ticket.setStatus(TicketStatus.CANCELLED);

		ticketRepository.save(ticket);

		return ticket;

	}

	@Transactional
	public Ticket approvalTicket(long id) {

		User currentUser = authenticationService.getCurrentUser();

		Ticket ticket = ticketRepository.findById(id)
			.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Ticket does not exits"));

		if (ticket.getApprover().getId() != currentUser.getId()
			&& approvalService.canApproveTimesheetAdjustment(currentUser.getId(),
				currentUser.getCurrentLevel())) {

			throw new BusinessException(HttpStatus.FORBIDDEN, "You do not have permission to approval this ticket");

		}

		ticket.setStatus(TicketStatus.CANCELLED);

		ticketRepository.save(ticket);

		return ticket;

	}

	private Ticket findTicketByCurrentUser(long id) {

		User currentUser = authenticationService.getCurrentUser();

		Ticket ticket = ticketRepository.findById(id)
			.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Ticket does not exits"));

		if (ticket.getCreator().getId() != currentUser.getId()) {

			throw new BusinessException(HttpStatus.FORBIDDEN, "You do not have permission to view this ticket");

		}

		return ticket;

	}
}
