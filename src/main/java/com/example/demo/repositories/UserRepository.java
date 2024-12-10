package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	// @Cacheable(value = "user", key = "#username")
	@Query("SELECT u FROM User u WHERE u.username = :username")
	Optional<User> findByUsername(String username);

	@Cacheable(value = "user:roles_permissions", key = "#username")
	@Query("SELECT u FROM User u LEFT JOIN FETCH u.roles r LEFT JOIN FETCH r.permissions p WHERE u.username = :username")
	Optional<User> findByUsernameWithRolesAndPermissions(String username);

}
