package com.example.demo.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.BusinessException;
import com.example.demo.models.User;
import com.example.demo.models.QRole;
import com.example.demo.models.QUser;
import com.example.demo.repositories.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	private final JPAQueryFactory factory;

	public Page<User> getAll(Pageable pageable) {

		QUser user = QUser.user;
		QRole role = QRole.role;

		// Fetch data with pagination
		List<User> users = factory.selectFrom(user)
			.leftJoin(user.roles, role).fetchJoin()
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		// Fetch total count
		long total = factory.select(user.count()).from(user).fetchOne();

		return new PageImpl<>(users, pageable, total);

	}

	@Transactional(readOnly = true)
	public User getById(long id) {

		return userRepository.findById(id)
			.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "User does not exits"));

	}

	public User createUser(User data) {

		User user = data;

		userRepository.save(data);

		return user;

	}

	public User updateUser(long id, User data) {

		User user = userRepository.findById(id)
			.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "User does not exits"));

		userRepository.save(data);

		return user;

	}

	public void deleteUserById(long id) {

		userRepository.deleteById(id);

	}

}
