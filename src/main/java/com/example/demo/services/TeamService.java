package com.example.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.Team;
import com.example.demo.repositories.TeamRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TeamService {

	private final TeamRepository teamRepository;

	@Transactional(readOnly = true)
	public List<Team> getAll() {

		return teamRepository.findAll();

	}

}
