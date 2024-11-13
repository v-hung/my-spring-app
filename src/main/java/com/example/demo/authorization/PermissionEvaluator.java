package com.example.demo.authorization;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.demo.models.Permission;
import com.example.demo.models.PermissionCheckType;
import com.example.demo.models.PermissionType;
import com.example.demo.services.AuthenticationService;

import lombok.RequiredArgsConstructor;

@Component("permissionEvaluator")
@RequiredArgsConstructor
public class PermissionEvaluator {

	private final AuthenticationService authenticationService;

	public boolean hasPermission(Authentication authentication, String resource, PermissionType permissionType,
		String[] value, PermissionCheckType permissionCheckType) {

		if (authentication == null || authentication instanceof AnonymousAuthenticationToken
			|| !authentication.isAuthenticated()) {

			return false;

		}

		UserDetails userDetails = (UserDetails)authentication.getPrincipal();

		var authorities = authenticationService.getAuthorities(userDetails.getUsername());

		boolean isAdmin = authorities.stream()
			.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));

		if (isAdmin) {

			return true;

		}

		if (value != null && value.length > 0) {

			return switch (permissionCheckType) {

			case ANY -> hasAnyPermission(authorities, value);
			case ALL -> hasAllPermissions(authorities, value);

			};

		}

		return authorities.stream()
			.anyMatch(grantedAuthority -> grantedAuthority.getAuthority()
				.equals(Permission.getName(resource, permissionType)));

	}

	private boolean hasAnyPermission(Collection<? extends GrantedAuthority> authorities, String[] permissions) {

		return Arrays.stream(permissions)
			.anyMatch(permission -> authorities.stream()
				.anyMatch(authority -> {

					String grantedAuthority = authority.getAuthority();

					if (!permission.contains("_")) {

						grantedAuthority = grantedAuthority.split("_")[0];

					}

					return grantedAuthority.equals(permission);

				}));

	}

	private boolean hasAllPermissions(Collection<? extends GrantedAuthority> authorities, String[] permissions) {

		return Arrays.stream(permissions)
			.allMatch(permission -> authorities.stream()
				.anyMatch(authority -> authority.getAuthority().equals(permission)));

	}
}
