package com.example.demo.controllers;

import org.modelmapper.ModelMapper;
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
import com.example.demo.models.PermissionType;
import com.example.demo.models.User;
import com.example.demo.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	private final ModelMapper modelMapper;

	@GetMapping()
	public ResponseEntity<Page<UserDto>> getUsers(@PageableDefault(size = 20) Pageable pageable) {

		Page<User> page = userService.getAll(pageable);

		Page<UserDto> users = page.map(user -> modelMapper.map(user, UserDto.class));

		return ResponseEntity.ok(users);

	}

	@GetMapping("/{id}")
	public ResponseEntity<UserDto> getUser(@PathVariable long id) {

		User user = userService.getById(id);

		return ResponseEntity.ok(modelMapper.map(user, UserDto.class));

	}

	@PostMapping()
	@HasPermission(PermissionType.USER_CREATE)
	public ResponseEntity<UserDto> createUser(@RequestBody User model) {

		User user = userService.createUser(model);

		return ResponseEntity.ok(modelMapper.map(user, UserDto.class));

	}

	@PutMapping("/{id}/edit")
	@PatchMapping("/{id}/edit")
	@HasPermission(PermissionType.USER_UPDATE)
	public ResponseEntity<UserDto> updateUser(@PathVariable long id, @RequestBody User model) {

		User user = userService.updateUser(id, model);

		return ResponseEntity.ok(modelMapper.map(user, UserDto.class));

	}

	@DeleteMapping("/{id}")
	@HasPermission(PermissionType.USER_DELETE)
	public ResponseEntity<String> deleteUser(@PathVariable long id) {

		userService.deleteUserById(id);

		return ResponseEntity.ok("User is deleted");

	}

}
