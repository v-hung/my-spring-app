package com.example.demo.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UserDto;
import com.example.demo.models.TicketType;
import com.example.demo.models.User;
import com.example.demo.services.ApprovalService;
import com.example.demo.services.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/approvals")
@RequiredArgsConstructor
public class ApprovalController {

	private final ApprovalService approvalService;

	private final AuthenticationService authenticationService;

	@GetMapping("/candidates")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<UserDto>> getCandidates(@RequestParam TicketType ticketType) {

		User currentUser = authenticationService.getCurrentUser();

		List<UserDto> users = approvalService.getCandidates(ticketType, currentUser);

		return ResponseEntity.ok(users);

	}
}
