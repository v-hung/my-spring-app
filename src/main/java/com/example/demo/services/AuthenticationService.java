package com.example.demo.services;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.UserDto;
import com.example.demo.models.RefreshToken;
import com.example.demo.models.User;
import com.example.demo.repositories.RefreshTokenRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.requests.LoginRequest;
import com.example.demo.requests.RefreshRequest;
import com.example.demo.responses.LoginResponse;
import com.example.demo.responses.RefreshResponse;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final AuthenticationManager authenticationManager;

	private final JwtService jwtService;

	private final UserRepository userRepository;

	private final RefreshTokenRepository refreshTokenRepository;

	private final ModelMapper modelMapper;

	public LoginResponse login(HttpServletResponse servletResponse, LoginRequest model) {

		Authentication authentication = authenticationManager
			.authenticate(new UsernamePasswordAuthenticationToken(model.getUsername(), model.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetails userDetails = (UserDetails)authentication.getPrincipal();

		User user = userRepository.findByEmail(userDetails.getUsername())
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		String token = jwtService.generateToken(authentication.getName());
		String refreshToken = jwtService.generateRefreshToken(authentication.getName());

		servletResponse.addHeader(HttpHeaders.SET_COOKIE, jwtService.generateTokenCookie(token).toString());
		servletResponse.addHeader(HttpHeaders.SET_COOKIE,
			jwtService.generateRefreshTokenCookie(refreshToken).toString());

		saveRefreshToken(refreshToken, user, model.isRemember());
		deleteExpiredTokens();

		return new LoginResponse()
			.setUser(modelMapper.map(user, UserDto.class))
			.setToken(token)
			.setRefreshToken(refreshToken);

	}

	public RefreshResponse refreshToken(HttpServletRequest request, HttpServletResponse servletResponse,
		RefreshRequest model) {

		try {

			final Cookie[] cookies = request.getCookies() != null ? request.getCookies() : new Cookie[0];

			String refreshTokenValue = Arrays.stream(cookies).filter(c -> c.getName().equals("refreshToken"))
				.map(Cookie::getValue).findFirst()
				.orElse(null);

			if (model != null && StringUtils.isNotBlank(model.getRefreshToken())) {

				refreshTokenValue = model.getRefreshToken();

			}

			RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh token not found"));

			final String username = jwtService.extractUsername(refreshTokenValue);

			String token = jwtService.generateToken(username);
			String newRefreshToken = jwtService.generateRefreshToken(username);

			servletResponse.addHeader(HttpHeaders.SET_COOKIE, jwtService.generateTokenCookie(token).toString());
			servletResponse.addHeader(HttpHeaders.SET_COOKIE,
				jwtService.generateRefreshTokenCookie(newRefreshToken).toString());

			refreshToken.setToken(newRefreshToken);
			refreshTokenRepository.save(refreshToken);

			return new RefreshResponse()
				.setToken(token)
				.setRefreshToken(newRefreshToken);

		} catch (Exception ex) {

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error refreshing token", ex);

		}

	}

	@Transactional(readOnly = true)
	public User getCurrentUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {

			Object principal = authentication.getPrincipal();

			if (principal instanceof UserDetails) {

				return (User)principal;

			} else {

				return null;

			}

		}

		return null;

	}

	@Transactional(readOnly = true)
	public <D> D getCurrentUser(Class<D> dtoClass) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {

			Object principal = authentication.getPrincipal();

			if (principal instanceof UserDetails) {

				return modelMapper.map(principal, dtoClass);

			} else {

				return null;

			}

		}

		return null;

	}

	private void saveRefreshToken(String refreshToken, User user, boolean remember) {

		RefreshToken refreshTokenEntity = new RefreshToken()
			.setToken(refreshToken)
			.setRemember(remember)
			.setUserId(user.getId())
			.setExpiryTime(jwtService.getRefreshExpirationLocalDateTime());

		refreshTokenRepository.save(refreshTokenEntity);

	}

	private void deleteExpiredTokens() {

		refreshTokenRepository.deleteExpiredTokens(LocalDateTime.now());

	}

}
