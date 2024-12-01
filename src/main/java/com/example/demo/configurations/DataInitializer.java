package com.example.demo.configurations;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	public DataInitializer(UserRepository userRepository, RoleRepository roleRepository,
		PasswordEncoder passwordEncoder) {

		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;

	}

	@Override
	public void run(String... args) throws Exception {

		log.info("Starting the database seeding process...");

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

		log.info("Database seeding process completed.");

	}

}
