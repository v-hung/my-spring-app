package com.example.demo.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.exception.BusinessException;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public List<User> getAll() {

		return userRepository.findAll();

	}

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
