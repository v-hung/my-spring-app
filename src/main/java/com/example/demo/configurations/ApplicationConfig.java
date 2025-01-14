package com.example.demo.configurations;

import org.hibernate.collection.spi.PersistentCollection;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.NameTokenizers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.repositories.UserRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module.Feature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

	private final UserRepository userRepository;

	private final EntityManager entityManager;

	@Bean
	ModelMapper modelMapper() {

		ModelMapper modelMapper = new ModelMapper();

		modelMapper.getConfiguration()
			.setSkipNullEnabled(true)
			.setPropertyCondition(context -> {

				// Skip lazy loading properties
				if (context.getSource() instanceof PersistentCollection<?> persistentCollection) {

					return !persistentCollection.wasInitialized();

				}

				return true;

			})
			.setFieldMatchingEnabled(true)
			.setFieldAccessLevel(AccessLevel.PRIVATE)
			.setSourceNameTokenizer(NameTokenizers.UNDERSCORE)
			.setDestinationNameTokenizer(NameTokenizers.UNDERSCORE);

		return modelMapper;

	}

	@Bean
	@Primary
	ObjectMapper objectMapper() {

		Hibernate6Module hibernate6Module = new Hibernate6Module();
		hibernate6Module.configure(Feature.FORCE_LAZY_LOADING, false);
		// Enable below line to switch lazy loaded json from null to a blank object!
		hibernate6Module.configure(Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS, true);

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.registerModule(hibernate6Module);
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		return objectMapper;

	}

	@Bean
	JPAQueryFactory jpaQueryFactory() {

		return new JPAQueryFactory(entityManager);

	}

	@Bean
	AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
		PasswordEncoder passwordEncoder) {

		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);

		return new ProviderManager(authenticationProvider);

	}

	@Bean
	UserDetailsService userDetailsService() {

		return username -> userRepository.findByEmailWithRolesAndPermissions(username)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));

	}

	@Bean
	PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();

	}

}
