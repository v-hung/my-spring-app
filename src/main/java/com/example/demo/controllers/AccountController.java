package com.example.demo.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UserDto;
import com.example.demo.models.User;
import com.example.demo.requests.LoginRequest;
import com.example.demo.requests.RefreshRequest;
import com.example.demo.responses.LoginResponse;
import com.example.demo.responses.RefreshResponse;
import com.example.demo.services.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

	private final AuthenticationService authenticationService;

	private final ModelMapper mapper;

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(HttpServletResponse response, @RequestBody LoginRequest model) {

		return ResponseEntity.ok(authenticationService.login(response, model));

	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/current")
	public ResponseEntity<UserDto> getCurrentUser() {

		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		return ResponseEntity.ok(mapper.map(user, UserDto.class));

	}

	@PostMapping("/refresh-token")
	public ResponseEntity<RefreshResponse> refreshToken(HttpServletResponse response, HttpServletRequest request,
		@RequestBody(required = false) RefreshRequest model) {

		return ResponseEntity.ok(authenticationService.refreshToken(request, response, model));

	}

}
