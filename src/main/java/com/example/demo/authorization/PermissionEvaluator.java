package com.example.demo.authorization;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.example.demo.models.PermissionCheckType;

@Component("permissionEvaluator")
public class PermissionEvaluator {

	public boolean hasPermission(Authentication authentication, String[] value,
		PermissionCheckType permissionCheckType) {

		if (authentication == null || authentication instanceof AnonymousAuthenticationToken
			|| !authentication.isAuthenticated()) {

			return false;

		}

		var authorities = authentication.getAuthorities();

		if (authorities.stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"))) {

			return true;

		}

		return switch (permissionCheckType) {

		case ANY -> hasAnyPermission(authorities, value);
		case ALL -> hasAllPermissions(authorities, value);

		};

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

	/**
	 * Has All Permissions
	 *
	 * @param authorities
	 * @param permissions
	 * @return
	 */
	private boolean hasAllPermissions(Collection<? extends GrantedAuthority> authorities, String[] permissions) {

		return Arrays.stream(permissions)
			.allMatch(permission -> authorities.stream()
				.anyMatch(authority -> authority.getAuthority().equals(permission)));

	}
}
