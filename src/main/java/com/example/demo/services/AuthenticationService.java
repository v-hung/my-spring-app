package com.example.demo.services;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
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

	private final ModelMapper mapper;

	public LoginResponse login(HttpServletResponse response, LoginRequest model) {

		Authentication authentication = authenticationManager
			.authenticate(new UsernamePasswordAuthenticationToken(model.getUsername(), model.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetails userDetails = (UserDetails)authentication.getPrincipal();

		User user = userRepository.findByUsername(userDetails.getUsername())
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		;

		String token = jwtService.generateToken(authentication.getName());
		String refreshToken = jwtService.generateRefreshToken(authentication.getName());

		response.addCookie(jwtService.generateTokenCookie(token));
		response.addCookie(jwtService.generateRefreshTokenCookie(refreshToken));

		saveRefreshToken(refreshToken, user, model.isRemember());
		deleteExpiredTokens();

		return new LoginResponse() {
			{

				setUser(mapper.map(user, UserDto.class));
				setToken(token);
				setRefreshToken(refreshToken);

			}
		};

	}

	public RefreshResponse refreshToken(HttpServletResponse response, HttpServletRequest request,
		RefreshRequest model) {

		try {

			final Cookie[] cookies = request.getCookies() != null ? request.getCookies() : new Cookie[0];

			String refreshTokenValue = Arrays.stream(cookies).filter(c -> c.getName().equals("refreshToken"))
				.map(Cookie::getValue).findFirst()
				.orElse(null);

			if (!model.getRefreshToken().isBlank()) {

				refreshTokenValue = model.getRefreshToken();

			}

			RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh token not found"));

			final String username = jwtService.extractUsername(refreshTokenValue);

			String token = jwtService.generateToken(username);
			String newRefreshToken = jwtService.generateRefreshToken(username);

			response.addCookie(jwtService.generateTokenCookie(token));
			response.addCookie(jwtService.generateRefreshTokenCookie(newRefreshToken));

			refreshToken.setToken(newRefreshToken);
			refreshTokenRepository.save(refreshToken);

			return new RefreshResponse() {
				{

					setToken(token);
					setRefreshToken(newRefreshToken);

				}
			};

		} catch (Exception ex) {

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error refreshing token", ex);

		}

	}

	private void saveRefreshToken(String refreshToken, User user, boolean remember) {

		RefreshToken refreshTokenEntity = new RefreshToken()
			.setToken(refreshToken)
			.setRemember(remember)
			.setUser(user)
			.setExpiryTime(jwtService.getRefreshExpirationLocalDateTime());

		refreshTokenRepository.save(refreshTokenEntity);

	}

	private void deleteExpiredTokens() {

		refreshTokenRepository.deleteExpiredTokens(LocalDateTime.now());

	}

}
