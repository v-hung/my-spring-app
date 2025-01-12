package com.example.demo.services;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.BusinessException;
import com.example.demo.models.User;
import com.example.demo.models.Profile;
import com.example.demo.models.QRole;
import com.example.demo.models.QUser;
import com.example.demo.models.Role;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.TeamRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.WorkTimeRepository;
import com.example.demo.requests.UserCreateUpdateRequest;
import com.example.demo.requests.UserSearchResponse;
import com.example.demo.utils.ObjectUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final TeamRepository teamRepository;

	private final WorkTimeRepository workTimeRepository;

	private final RoleRepository roleRepository;

	private final JPAQueryFactory factory;

	private final ModelMapper modelMapper;

	@Transactional(readOnly = true)
	public <D> Page<D> getAll(Pageable pageable, UserSearchResponse search, Class<D> dtoClass) {

		int pageNumber = Math.max(pageable.getPageNumber(), 1);

		QUser user = QUser.user;
		QRole role = QRole.role;

		// Fetch data with pagination and search by name if provided
		BooleanBuilder predicate = new BooleanBuilder();

		if (search != null) {

			if (StringUtils.isNotBlank(search.getName())) {

				predicate.and(user.name.containsIgnoreCase(search.getName()));

			}

		}

		// Fetch data with pagination
		List<User> users = factory.selectFrom(user)
			.leftJoin(user.roles, role).fetchJoin()
			.where(role.admin.eq(false).and(predicate))
			.offset((pageNumber - 1L) * pageable.getPageSize())
			.limit(pageable.getPageSize())
			.fetch();

		// Fetch total count
		long total = factory.select(user.countDistinct())
			.from(user)
			.leftJoin(user.roles, role)
			.where(role.admin.eq(false).and(predicate))
			.fetchOne();

		List<D> userDto = users.stream().map(u -> modelMapper.map(u, dtoClass)).toList();

		return new PageImpl<>(userDto, pageable, total);

	}

	@Transactional(readOnly = true)
	public <D> D getById(long id, Class<D> dtoClass) {

		User user = userRepository.findById(id)
			.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "User does not exits"));

		return modelMapper.map(user, dtoClass);

	}

	@Transactional
	public <D> D createUser(UserCreateUpdateRequest data, Class<D> dtoClass) {

		User user = new User();

		setUserInformation(user, data);

		userRepository.save(user);

		return modelMapper.map(user, dtoClass);

	}

	@Transactional
	public <D> D updateUser(long id, UserCreateUpdateRequest data, Class<D> dtoClass) {

		User user = userRepository.findById(id)
			.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "User does not exits"));

		setUserInformation(user, data);

		userRepository.save(user);

		return modelMapper.map(user, dtoClass);

	}

	public void deleteUserById(long id) {

		userRepository.deleteById(id);

	}

	private void setUserInformation(User user, UserCreateUpdateRequest data) {

		user.setName(data.getName())
			.setEmail(data.getEmail())
			.setPosition(data.getPosition());

		if (data.getPassword() != null) {

			user.setPassword(passwordEncoder.encode(data.getPassword()));

		}

		if (data.getSupervisorId() != null) {

			user.setSupervisor(userRepository.findById(data.getSupervisorId()).orElse(null));

		}

		if (data.getTeamId() != null) {

			teamRepository.findById(data.getTeamId()).ifPresent(user::setTeam);

		}

		if (data.getWorkTimeId() != null) {

			workTimeRepository.findById(data.getWorkTimeId()).ifPresent(user::setWorkTime);

		}

		if (!data.getRoleIds().isEmpty()) {

			List<Role> roles = roleRepository.findAllById(data.getRoleIds());

			user.getRoles().clear();
			user.getRoles().addAll(roles);

		}

		if (ObjectUtils.hasNonNullProperties(data.getProfile())) {

			Profile profile = modelMapper.map(data.getProfile(), Profile.class);

			user.setProfile(profile);

		}

	}

}
