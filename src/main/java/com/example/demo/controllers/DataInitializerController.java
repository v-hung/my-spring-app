package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.Permission;
import com.example.demo.models.PermissionType;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.models.UserPosition;
import com.example.demo.repositories.PermissionRepository;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DataInitializerController {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final PermissionRepository permissionRepository;

	@GetMapping("/initialize-data")
	public String initializeData() {

		log.info("Starting the database seeding process...");

		// Call the method to seed admin user and roles/permissions
		seedAdminUser();
		Map<String, Role> roleMap = seedRolesAndPermissions();

		seedTestUsers(roleMap);

		log.info("Database seeding process completed.");
		return "Database seeding completed.";

	}

	private void seedAdminUser() {

		final String DEFAULT_USERNAME = "admin@admin.com";
		final String DEFAULT_PASSWORD = "Admin123!";

		Role userRole = roleRepository.findByName("admin")
			.orElseGet(() -> roleRepository.save(new Role().setName("admin").setAdmin(true)));

		if (!userRepository.findByUsername(DEFAULT_USERNAME).isPresent()) {

			User admin = new User()
				.setUsername(DEFAULT_USERNAME)
				.setName("Admin")
				.setEmail(DEFAULT_USERNAME)
				.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD)) // NOSONAR
				.setRoles(Arrays.asList(userRole));
			userRepository.save(admin);

		}

	}

	private Map<String, Role> seedRolesAndPermissions() {

		// Seed permissions
		List<PermissionType> permissionTypes = Arrays.asList(PermissionType.values());

		Map<String, Permission> permissionMap = permissionTypes.stream()
			.collect(Collectors.toMap(
				PermissionType::name,
				permission -> permissionRepository
					.findByName(permission)
					.orElseGet(() -> permissionRepository.save(new Permission().setName(permission)))));

		// Seed roles
		List<Role> roles = new ArrayList<>(List.of(
			new Role().setName("user").setLevel(1),
			new Role().setName("leader").setLevel(2), // NOSONAR
			new Role().setName("hr").setLevel(3)));

		Map<String, Role> roleMap = roles.stream()
			.collect(Collectors.toMap(
				Role::getName,
				role -> roleRepository
					.findByName(role.getName())
					.orElseGet(() -> roleRepository.save(role))));

		// Assign permissions to roles
		assignPermissionsToRoles(roleMap, permissionMap);

		return roleMap;

	}

	private void assignPermissionsToRoles(Map<String, Role> roleMap, Map<String, Permission> permissionMap) {

		Map<String, List<PermissionType>> rolePermissions = Map.of(
			"hr", new ArrayList<>(List.of(
				PermissionType.TIMESHEET_VIEW,
				PermissionType.TIMESHEET_CREATE,
				PermissionType.TIMESHEET_APPROVAL,
				PermissionType.USER_VIEW,
				PermissionType.USER_CREATE,
				PermissionType.USER_DELETE,
				PermissionType.USER_UPDATE)),
			"user", new ArrayList<>(List.of(
				PermissionType.TIMESHEET_VIEW,
				PermissionType.TIMESHEET_CREATE,
				PermissionType.USER_VIEW,
				PermissionType.USER_UPDATE)),
			"leader", new ArrayList<>(List.of(
				PermissionType.TIMESHEET_VIEW,
				PermissionType.TIMESHEET_CREATE,
				PermissionType.TIMESHEET_APPROVAL,
				PermissionType.USER_VIEW,
				PermissionType.USER_UPDATE)));

		rolePermissions.forEach((roleName, permissions) -> {

			Role role = roleMap.get(roleName);

			role.setPermissions(new ArrayList<>(permissions.stream()
				.map(permissionType -> permissionMap.get(permissionType.name()))
				.toList()));
			roleRepository.save(role);

		});

	}

	private void seedTestUsers(Map<String, Role> roleMap) {

		final String DEFAULT_PASSWORD = passwordEncoder.encode("password"); // NOSONAR

		List<User> users = new ArrayList<>(List.of(
			new User()
				.setName("hung")
				.setUsername("hung@test.com")
				.setEmail("hung@test.com")
				.setPassword(DEFAULT_PASSWORD)
				.setPosition(UserPosition.DEVELOPER)
				.setRoles(new ArrayList<>(List.of(roleMap.get("user")))),

			new User()
				.setName("tung")
				.setUsername("tung@test.com")
				.setEmail("tung@test.com")
				.setPassword(DEFAULT_PASSWORD)
				.setPosition(UserPosition.DEVELOPER)
				.setRoles(new ArrayList<>(List.of(roleMap.get("user")))),

			new User()
				.setName("manh")
				.setUsername("manh@test.com")
				.setEmail("manh@test.com")
				.setPassword(DEFAULT_PASSWORD)
				.setPosition(UserPosition.TEACH_LEADER)
				.setRoles(new ArrayList<>(List.of(roleMap.get("leader")))),

			new User()
				.setName("phuc")
				.setUsername("phuc@test.com")
				.setEmail("phuc@test.com")
				.setPassword(DEFAULT_PASSWORD)
				.setPosition(UserPosition.TEACH_LEADER)
				.setRoles(new ArrayList<>(List.of(roleMap.get("leader")))),

			new User()
				.setName("ha")
				.setUsername("ha@test.com")
				.setEmail("ha@test.com")
				.setPassword(DEFAULT_PASSWORD)
				.setPosition(UserPosition.HR_MANAGER)
				.setRoles(new ArrayList<>(List.of(roleMap.get("hr"))))));

		users.forEach(user -> userRepository.findByUsername(user.getUsername())
			.orElseGet(() -> userRepository.save(user)));

	}

}
