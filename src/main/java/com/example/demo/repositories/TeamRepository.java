package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {

	Optional<Team> findByName(String name);

}
