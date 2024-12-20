package com.example.demo.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	@Modifying
	@Transactional
	@Query("DELETE FROM RefreshToken rt WHERE rt.expiryTime < :currentTime")
	void deleteExpiredTokens(LocalDateTime currentTime);

	Optional<RefreshToken> findByToken(String token);

}
