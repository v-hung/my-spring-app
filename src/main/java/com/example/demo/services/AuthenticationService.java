package com.example.demo.services;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UserDto;
import com.example.demo.models.RefreshToken;
import com.example.demo.models.User;
import com.example.demo.repositories.RefreshTokenRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.requests.LoginRequest;
import com.example.demo.responses.LoginResponse;

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

		saveRefreshToken(refreshToken, model.isRemember());
		deleteExpiredTokens();

		return new LoginResponse() {
			{

				setUser(mapper.map(user, UserDto.class));
				setToken(token);
				setRefreshToken(refreshToken);

			}
		};

	}

	@Cacheable(value = "userAuthorities", key = "#username")
	public Collection<? extends GrantedAuthority> getAuthorities(String username) {

		return userRepository.findByUsername(username)
			.map(User::getAuthorities)
			.orElse(new HashSet<>());

	}

	private void saveRefreshToken(String refreshToken, boolean remember) {

		RefreshToken refreshTokenEntity = new RefreshToken()
			.setToken(refreshToken)
			.setRemember(remember)
			.setExpiryTime(jwtService.getRefreshExpirationLocalDateTime());

		refreshTokenRepository.save(refreshTokenEntity);

	}

	private void deleteExpiredTokens() {

		refreshTokenRepository.deleteExpiredTokens(LocalDateTime.now());

	}

}
