package com.example.demo.controllers;

import java.util.List;

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

import com.example.demo.dto.RoleDto;
import com.example.demo.requests.RoleCreateUpdateRequest;
import com.example.demo.services.RoleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

	private final RoleService roleService;

	@GetMapping()
	public ResponseEntity<List<RoleDto>> getRoles() {

		List<RoleDto> roles = roleService.getAll(RoleDto.class);

		return ResponseEntity.ok(roles);

	}

	@GetMapping("/{id}")
	public ResponseEntity<RoleDto> getRole(@PathVariable long id) {

		RoleDto role = roleService.getById(id, RoleDto.class);

		return ResponseEntity.ok(role);

	}

	@PostMapping()
	public ResponseEntity<RoleDto> createRole(@RequestBody RoleCreateUpdateRequest model) {

		RoleDto role = roleService.createRole(model, RoleDto.class);

		return ResponseEntity.ok(role);

	}

	@PutMapping("/{id}/edit")
	@PatchMapping("/{id}/edit")
	public ResponseEntity<RoleDto> updateRole(@PathVariable long id, @RequestBody RoleCreateUpdateRequest model) {

		RoleDto role = roleService.updateRole(id, model, RoleDto.class);

		return ResponseEntity.ok(role);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteRole(@PathVariable long id) {

		roleService.deleteRoleById(id);

		return ResponseEntity.ok("Role is deleted");

	}

}
