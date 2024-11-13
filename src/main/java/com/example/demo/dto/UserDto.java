package com.example.demo.dto;

import java.util.HashSet;
import java.util.Set;

import com.example.demo.models.Role;

import lombok.Data;

@Data
public class UserDto {

	private Long id;

	private String name;

	private String username;

	private String email;

	private Set<Role> roles = new HashSet<>();
}
