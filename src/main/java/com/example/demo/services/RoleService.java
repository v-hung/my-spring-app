package com.example.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.BusinessException;
import com.example.demo.models.Permission;
import com.example.demo.models.Role;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.requests.RoleCreateUpdateRequest;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoleService {

	private final RoleRepository roleRepository;

	private final ModelMapper modelMapper;

	@Transactional(readOnly = true)
	public <D> List<D> getAll(Class<D> dtoClass) {

		List<Role> roles = roleRepository.findAll();

		return roles.stream().map(t -> modelMapper.map(t, dtoClass)).toList();

	}

	@Transactional(readOnly = true)
	public <D> D getById(long id, Class<D> dtoClass) {

		Role role = roleRepository.findById(id)
			.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Role does not exits"));

		return modelMapper.map(role, dtoClass);

	}

	@Transactional
	public <D> D createRole(RoleCreateUpdateRequest data, Class<D> dtoClass) {

		Role role = new Role();

		setRoleInformation(role, data);

		roleRepository.save(role);

		return modelMapper.map(role, dtoClass);

	}

	@Transactional
	public <D> D updateRole(long id, RoleCreateUpdateRequest data, Class<D> dtoClass) {

		Role role = roleRepository.findById(id)
			.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Role does not exits"));

		setRoleInformation(role, data);

		roleRepository.save(role);

		return modelMapper.map(role, dtoClass);

	}

	public void deleteRoleById(long id) {

		roleRepository.deleteById(id);

	}

	private void setRoleInformation(Role role, RoleCreateUpdateRequest data) {

		role.setName(data.getName())
			.setDescription(data.getDescription())
			.setAdmin(data.isAdmin())
			.setLevel(data.getLevel())
			.setPermissions(data.getPermissions().stream().map(p -> modelMapper.map(p, Permission.class))
				.collect(Collectors.toSet()));

	}

}
