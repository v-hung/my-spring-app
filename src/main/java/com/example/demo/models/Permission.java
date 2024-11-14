package com.example.demo.models;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Data
@Accessors(chain = true)
public class Permission implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String resource;

	@Enumerated(EnumType.STRING)
	private PermissionType permissionType;

	public String getName() {

		return getName(resource, permissionType);

	}

	public static String getName(String resource, PermissionType permissionType) {

		return String.format("%s_%s", resource, permissionType.name()).toUpperCase();

	}

}
