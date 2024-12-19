package com.example.demo.authorization;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.demo.models.PermissionType;

@Component("permissionEvaluator")
public class PermissionEvaluator {

	public boolean hasPermission(Authentication authentication, PermissionType permission) {

		if (authentication == null || authentication instanceof AnonymousAuthenticationToken
			|| !authentication.isAuthenticated()) {

			return false;

		}

		var authorities = authentication.getAuthorities();

		if (authorities.stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"))) {

			return true;

		}

		return authorities.stream().anyMatch(authority -> authority.getAuthority().equals(permission.name()));

	}

}
