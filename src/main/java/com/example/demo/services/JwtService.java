package com.example.demo.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import lombok.Getter;

@Service
@Getter
public class JwtService {

	@Value("${spring.application.security.jwt.secret-key}")
	private String secretKey;

	@Value("${spring.application.security.jwt.expiration}")
	private long jwtExpiration;

	@Value("${spring.application.security.jwt.refresh-token.expiration}")
	private long refreshExpiration;

	public String extractUsername(String token) {

		return extractClaim(token, Claims::getSubject);

	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);

	}

	public String generateToken(String username) {

		return generateToken(new HashMap<>(), username);

	}

	public String generateToken(Map<String, Object> extraClaims, String username) {

		return buildToken(extraClaims, username, jwtExpiration);

	}

	public String generateRefreshToken(String username) {

		return buildToken(new HashMap<>(), username, refreshExpiration);

	}

	public String generateRefreshToken(String username, Date expiration) {

		return Jwts.builder().claims(new HashMap<>()).subject(username).issuedAt(new Date(System.currentTimeMillis()))
			.expiration(expiration).signWith(getSignInKey()).compact();

	}

	private String buildToken(Map<String, Object> extraClaims, String username, long expiration) {

		return Jwts.builder().claims(extraClaims).subject(username).issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + expiration)).signWith(getSignInKey()).compact();

	}

	public boolean isTokenValid(String token, String username) {

		final String extractedUsername = extractUsername(token);
		return (extractedUsername.equals(username)) && !isTokenExpired(token);

	}

	private boolean isTokenExpired(String token) {

		return extractExpiration(token).before(new Date());

	}

	private Date extractExpiration(String token) {

		return extractClaim(token, Claims::getExpiration);

	}

	private Claims extractAllClaims(String token) {

		return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();

	}

	private SecretKey getSignInKey() {

		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);

	}

	public Date getRefreshExpirationDate() {

		return new Date(System.currentTimeMillis() + refreshExpiration);

	}

	public LocalDateTime getRefreshExpirationLocalDateTime() {

		return getRefreshExpirationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

	}

	public Cookie generateTokenCookie(String token) {

		return new Cookie("token", token) {
			{

				setHttpOnly(true);
				setPath("/");
				setMaxAge((int)jwtExpiration);

			}
		};

	}

	public Cookie generateRefreshTokenCookie(String refreshToken) {

		return new Cookie("refreshToken", refreshToken) {
			{

				setHttpOnly(true);
				setPath("/");
				setMaxAge((int)refreshExpiration);

			}
		};

	}

}
