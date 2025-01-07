package com.example.demo.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.BusinessException;
import com.example.demo.models.Team;
import com.example.demo.repositories.TeamRepository;
import com.example.demo.requests.TeamCreateUpdateRequest;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TeamService {

	private final TeamRepository teamRepository;

	private final ModelMapper modelMapper;

	@Transactional(readOnly = true)
	public <D> List<D> getAll(Class<D> dtoClass) {

		List<Team> teams = teamRepository.findAll();

		return teams.stream().map(t -> modelMapper.map(t, dtoClass)).toList();

	}

	@Transactional(readOnly = true)
	public <D> D getById(long id, Class<D> dtoClass) {

		Team team = teamRepository.findById(id)
			.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Team does not exits"));

		return modelMapper.map(team, dtoClass);

	}

	@Transactional
	public <D> D createTeam(TeamCreateUpdateRequest data, Class<D> dtoClass) {

		Team team = new Team();

		setTeamInformation(team, data);

		teamRepository.save(team);

		return modelMapper.map(team, dtoClass);

	}

	@Transactional
	public <D> D updateTeam(long id, TeamCreateUpdateRequest data, Class<D> dtoClass) {

		Team team = teamRepository.findById(id)
			.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Team does not exits"));

		setTeamInformation(team, data);

		teamRepository.save(team);

		return modelMapper.map(team, dtoClass);

	}

	public void deleteTeamById(long id) {

		teamRepository.deleteById(id);

	}

	private void setTeamInformation(Team team, TeamCreateUpdateRequest data) {

		team.setName(data.getName())
			.setDescription(data.getDescription());

	}

}
