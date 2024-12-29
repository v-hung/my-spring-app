package com.example.demo.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.TeamDto;
import com.example.demo.models.Team;
import com.example.demo.services.TeamService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

	private final TeamService teamService;

	private final ModelMapper modelMapper;

	@GetMapping()
	public ResponseEntity<List<TeamDto>> getTeams() {

		List<Team> teams = teamService.getAll();

		List<TeamDto> teamDtos = teams.stream().map(team -> modelMapper.map(team, TeamDto.class)).toList();

		return ResponseEntity.ok(teamDtos);

	}

}
