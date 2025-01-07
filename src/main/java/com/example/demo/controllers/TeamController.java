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

import com.example.demo.dto.TeamDto;
import com.example.demo.requests.TeamCreateUpdateRequest;
import com.example.demo.services.TeamService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

	private final TeamService teamService;

	@GetMapping()
	public ResponseEntity<List<TeamDto>> getTeams() {

		List<TeamDto> teams = teamService.getAll(TeamDto.class);

		return ResponseEntity.ok(teams);

	}

	@GetMapping("/{id}")
	public ResponseEntity<TeamDto> getTeam(@PathVariable long id) {

		TeamDto team = teamService.getById(id, TeamDto.class);

		return ResponseEntity.ok(team);

	}

	@PostMapping()
	public ResponseEntity<TeamDto> createTeam(@RequestBody TeamCreateUpdateRequest model) {

		TeamDto team = teamService.createTeam(model, TeamDto.class);

		return ResponseEntity.ok(team);

	}

	@PutMapping("/{id}/edit")
	@PatchMapping("/{id}/edit")
	public ResponseEntity<TeamDto> updateTeam(@PathVariable long id, @RequestBody TeamCreateUpdateRequest model) {

		TeamDto team = teamService.updateTeam(id, model, TeamDto.class);

		return ResponseEntity.ok(team);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteTeam(@PathVariable long id) {

		teamService.deleteTeamById(id);

		return ResponseEntity.ok("Team is deleted");

	}

}
