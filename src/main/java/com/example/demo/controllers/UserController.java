package com.example.demo.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.authorization.HasPermission;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserFullDto;
import com.example.demo.models.PermissionType;
import com.example.demo.requests.UserCreateUpdateRequest;
import com.example.demo.requests.UserSearchResponse;
import com.example.demo.responses.PageResponse;
import com.example.demo.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping()
	public ResponseEntity<PageResponse<UserDto>> getUsers(@PageableDefault(size = 20) Pageable pageable,
		UserSearchResponse model) {

		Page<UserDto> page = userService.getAll(pageable, model, UserDto.class);

		return ResponseEntity.ok(new PageResponse<UserDto>(page));

	}

	@GetMapping("/{id}")
	public ResponseEntity<UserDto> getUser(@PathVariable long id) {

		UserDto user = userService.getById(id, UserDto.class);

		return ResponseEntity.ok(user);

	}

	@GetMapping("/{id}/details")
	public ResponseEntity<UserFullDto> getUserDetails(@PathVariable long id) {

		UserFullDto user = userService.getById(id, UserFullDto.class);

		return ResponseEntity.ok(user);

	}

	@PostMapping()
	@HasPermission(PermissionType.USER_CREATE)
	public ResponseEntity<UserDto> createUser(@RequestBody UserCreateUpdateRequest model) {

		UserDto user = userService.createUser(model, UserDto.class);

		return ResponseEntity.ok(user);

	}

	@PutMapping("/{id}/edit")
	@PatchMapping("/{id}/edit")
	@HasPermission(PermissionType.USER_UPDATE)
	public ResponseEntity<UserDto> updateUser(@PathVariable long id, @RequestBody UserCreateUpdateRequest model) {

		UserDto user = userService.updateUser(id, model, UserDto.class);

		return ResponseEntity.ok(user);

	}

	@DeleteMapping("/{id}")
	@HasPermission(PermissionType.USER_DELETE)
	public ResponseEntity<String> deleteUser(@PathVariable long id) {

		userService.deleteUserById(id);

		return ResponseEntity.ok("User is deleted");

	}

}
