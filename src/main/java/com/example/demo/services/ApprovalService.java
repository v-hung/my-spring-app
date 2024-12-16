package com.example.demo.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.models.Role;
import com.example.demo.models.TicketType;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApprovalService {

	private final UserRepository userRepository;

	public List<User> getCandidates(TicketType ticketType, User user) {

		switch (ticketType) {

		case TIMESHEET_ADJUSTMENT:

			return getCandidatesWithTimesheetAdjustment(user);
		case LEAVE_REQUEST:

			return getCandidatesWithTimesheetAdjustment(user);

		default:
			return new ArrayList<>();

		}

	}

	private List<User> getCandidatesWithTimesheetAdjustment(User user) {

		int currentLevel = user.getRoles().stream().map(Role::getLevel).max(Comparator.naturalOrder()).orElse(1);

		return userRepository.findByRoles_Permissions_NameAndRoles_Level("timesheet_approval", currentLevel + 1);

	}

}
