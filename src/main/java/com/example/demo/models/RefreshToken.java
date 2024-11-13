package com.example.demo.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Data
@Accessors(chain = true)
public class RefreshToken {

	@Id
	private String token;

	private boolean remember = false;

	@NotNull
	private LocalDateTime expiryTime;

}
