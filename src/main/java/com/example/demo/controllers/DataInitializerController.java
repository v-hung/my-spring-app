package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.Permission;
import com.example.demo.models.PermissionType;
import com.example.demo.models.Role;
import com.example.demo.models.Team;
import com.example.demo.models.User;
import com.example.demo.models.UserPosition;
import com.example.demo.models.WorkTime;
import com.example.demo.repositories.PermissionRepository;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.TeamRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.WorkTimeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
	private final TeamRepository teamRepository;
	private final WorkTimeRepository workTimeRepository;

	@GetMapping("/initialize-data")
	public String initializeData() {

		log.info("Starting the database seeding process...");

		// Call the method to seed admin user and roles/permissions
		seedAdminUser();
		Map<String, Role> roleMap = seedRolesAndPermissions();
		Map<String, Team> teamMap = seedTeams();
		seedWorkTimes();

		seedTestUsers(roleMap, teamMap);

		log.info("Database seeding process completed.");
		return "Database seeding completed.";

	}

	private void seedAdminUser() {

		final String DEFAULT_USERNAME = "admin@admin.com";
		final String DEFAULT_PASSWORD = "Admin123!";

		Role userRole = roleRepository.findByName("admin")
			.orElseGet(() -> roleRepository.save(new Role().setName("admin").setAdmin(true)));

		if (!userRepository.findByEmail(DEFAULT_USERNAME).isPresent()) {

			User admin = new User()
				.setName("Admin")
				.setEmail(DEFAULT_USERNAME)
				.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD)) // NOSONAR
				.setRoles(new HashSet<>(Arrays.asList(userRole)));
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
		List<Role> roles = List.of(
			new Role().setName("user").setLevel(1),
			new Role().setName("leader").setLevel(2), // NOSONAR
			new Role().setName("hr").setLevel(3));

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
			"hr", List.of(
				PermissionType.TIMESHEET_VIEW,
				PermissionType.TIMESHEET_CREATE,
				PermissionType.TIMESHEET_APPROVAL,
				PermissionType.USER_VIEW,
				PermissionType.USER_CREATE,
				PermissionType.USER_DELETE,
				PermissionType.USER_UPDATE),
			"user", List.of(
				PermissionType.TIMESHEET_VIEW,
				PermissionType.TIMESHEET_CREATE,
				PermissionType.USER_VIEW,
				PermissionType.USER_UPDATE),
			"leader", List.of(
				PermissionType.TIMESHEET_VIEW,
				PermissionType.TIMESHEET_CREATE,
				PermissionType.TIMESHEET_APPROVAL,
				PermissionType.USER_VIEW,
				PermissionType.USER_UPDATE));

		rolePermissions.forEach((roleName, permissions) -> {

			Role role = roleMap.get(roleName);
			role.setPermissions(permissions.stream()
				.map(permissionType -> permissionMap.get(permissionType.name()))
				.collect(Collectors.toSet()));
			roleRepository.save(role);

		});

	}

	private Map<String, Team> seedTeams() {

		List<Team> teams = List.of(
			new Team().setName("STNET"), // NOSONAR
			new Team().setName("MSR"),
			new Team().setName("VMO"));

		return teams.stream()
			.collect(Collectors.toMap(
				Team::getName,
				team -> teamRepository
					.findByName(team.getName(), Team.class)
					.orElseGet(() -> teamRepository.save(team))));

	}

	private Map<String, WorkTime> seedWorkTimes() {

		List<WorkTime> workTimes = List.of(
			new WorkTime().setTitle("Cơ bản"),
			new WorkTime().setTitle("Làm việc sớm")
				.setStartTimeAfternoon(LocalTime.parse("13:00:00"))
				.setEndTimeAfternoon(LocalTime.parse("17:00:00")));

		return workTimes.stream()
			.collect(Collectors.toMap(
				WorkTime::getTitle,
				workTime -> workTimeRepository
					.findByTitle(workTime.getTitle())
					.orElseGet(() -> workTimeRepository.save(workTime))));

	}

	private void seedTestUsers(Map<String, Role> roleMap, Map<String, Team> teamMap) {

		final String DEFAULT_PASSWORD = passwordEncoder.encode("password"); // NOSONAR

		List<User> users = List.of(
			new User()
				.setName("hung")
				.setEmail("hung@test.com")
				.setPassword(DEFAULT_PASSWORD)
				.setPosition(UserPosition.DEVELOPER)
				.setTeam(teamMap.get("STNET"))
				.setRoles(Set.of(roleMap.get("user"))),

			new User()
				.setName("tung")
				.setEmail("tung@test.com")
				.setPassword(DEFAULT_PASSWORD)
				.setPosition(UserPosition.DEVELOPER)
				.setTeam(teamMap.get("MSR"))
				.setRoles(Set.of(roleMap.get("user"))),

			new User()
				.setName("manh")
				.setEmail("manh@test.com")
				.setPassword(DEFAULT_PASSWORD)
				.setPosition(UserPosition.TEACH_LEADER)
				.setTeam(teamMap.get("STNET"))
				.setRoles(Set.of(roleMap.get("leader"))),

			new User()
				.setName("phuc")
				.setEmail("phuc@test.com")
				.setPassword(DEFAULT_PASSWORD)
				.setPosition(UserPosition.TEACH_LEADER)
				.setRoles(Set.of(roleMap.get("leader"))),

			new User()
				.setName("ha")
				.setEmail("ha@test.com")
				.setPassword(DEFAULT_PASSWORD)
				.setPosition(UserPosition.HR_MANAGER)
				.setTeam(teamMap.get("VMO"))
				.setRoles(Set.of(roleMap.get("hr"))));

		users.forEach(user -> userRepository.findByEmail(user.getUsername())
			.orElseGet(() -> userRepository.save(user)));

	}

}
