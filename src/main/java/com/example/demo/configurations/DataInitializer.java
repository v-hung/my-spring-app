package com.example.demo.configurations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.models.Permission;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repositories.PermissionRepository;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final PermissionRepository permissionRepository;

	@Override
	public void run(String... args) throws Exception {

		log.info("Starting the database seeding process...");

		// seedAdminUser();

		// seedRolesAndPermissions();

		log.info("Database seeding process completed.");

	}

	private void seedAdminUser() {

		Role userRole = roleRepository.findByName("admin")
			.orElseGet(() -> roleRepository.save(new Role().setName("admin").setAdmin(true)));

		final String DEFAULT_USERNAME = "admin@admin.com";

		if (!userRepository.findByUsername(DEFAULT_USERNAME).isPresent()) {

			User admin = new User()
				.setUsername(DEFAULT_USERNAME)
				.setName("Admin")
				.setEmail(DEFAULT_USERNAME)
				.setPassword(passwordEncoder.encode("Admin123!")) // NOSONAR
				.setRoles(new HashSet<>(Arrays.asList(userRole)));
			userRepository.save(admin);

		}

	}

	private void seedRolesAndPermissions() {

		List<String> permissions = Arrays.asList(
			"timesheet_view", "timesheet_create",
			"inbox_view");

		permissions.forEach(permission -> permissionRepository
			.findByName(permission)
			.orElseGet(() -> permissionRepository.save(new Permission().setName(permission))));

		List<String> roles = Arrays.asList("hr", "user", "leader");

		roles.forEach(role -> roleRepository
			.findByName(role)
			.orElseGet(() -> roleRepository.save(new Role().setName(role))));

		// assignPermissionsToRoles();

	}

	private void assignPermissionsToRoles() {

		Role hrRole = roleRepository.findByName("hr").orElseThrow();
		Role userRole = roleRepository.findByName("user").orElseThrow();
		Role leaderRole = roleRepository.findByName("leader").orElseThrow();

		hrRole.setPermissions(new HashSet<>(Arrays.asList(
			permissionRepository.findByName("view_user").orElseThrow(),
			permissionRepository.findByName("edit_user").orElseThrow(),
			permissionRepository.findByName("deploy_code").orElseThrow(),
			permissionRepository.findByName("access_code_repo").orElseThrow())));
		roleRepository.save(hrRole);

		userRole.setPermissions(new HashSet<>(Arrays.asList(
			permissionRepository.findByName("view_project").orElseThrow(),
			permissionRepository.findByName("assign_project").orElseThrow())));
		roleRepository.save(userRole);

		leaderRole.setPermissions(new HashSet<>(Arrays.asList(
			permissionRepository.findByName("perform_testing").orElseThrow())));
		roleRepository.save(leaderRole);

	}

}
