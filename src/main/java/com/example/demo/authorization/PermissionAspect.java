package com.example.demo.authorization;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class PermissionAspect {

	private final PermissionEvaluator permissionEvaluator;

	@Before("@annotation(hasPermission)")
	public void checkPermission(JoinPoint joinPoint, HasPermission hasPermission) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		boolean hasAccess = permissionEvaluator.hasPermission(
			authentication,
			hasPermission.value(),
			hasPermission.permissionCheckType());

		if (!hasAccess) {

			throw new AccessDeniedException("Access is denied");

		}

	}

}
