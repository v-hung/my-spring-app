package com.example.demo.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.BusinessException;
import com.example.demo.models.PermissionType;
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

		return userRepository.findByRoles_Permissions_NameAndRoles_Level(PermissionType.TIMESHEET_APPROVAL,
			currentLevel + 1);

	}

	@Transactional(readOnly = true)
	public boolean canApproveTimesheetAdjustment(long approverId, long requiredLevel) {

		User approverUser = userRepository.findById(approverId).orElseThrow(
			() -> new BusinessException(HttpStatus.NOT_FOUND, "User is not found"));

		return approverUser.getRoles().stream()
			.filter(role -> role.getLevel() > requiredLevel)
			.flatMap(role -> role.getPermissions().stream())
			.anyMatch(permission -> permission.getName().equals(PermissionType.TIMESHEET_APPROVAL));

	}

}
