package com.example.demo.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.BusinessException;
import com.example.demo.models.QTeam;
import com.example.demo.models.QUser;
import com.example.demo.models.Team;
import com.example.demo.models.User;
import com.example.demo.repositories.TeamRepository;
import com.example.demo.requests.TeamCreateUpdateRequest;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TeamService {

	private final TeamRepository teamRepository;

	private final ModelMapper modelMapper;

	private final JPAQueryFactory factory;

	@Transactional(readOnly = true)
	public <D> List<D> getAll(Class<D> dtoClass) {

		QTeam team = QTeam.team;
		QUser user = QUser.user;

		JPQLQuery<User> membersSubquery = JPAExpressions
			.select(user)
			.from(user)
			.where(user.team.id.eq(team.id))
			.limit(3);

		// Query chính
		List<Team> teams = factory.selectFrom(team)
			.leftJoin(team.members)
			.where(user.in(membersSubquery))
			.fetch();

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
