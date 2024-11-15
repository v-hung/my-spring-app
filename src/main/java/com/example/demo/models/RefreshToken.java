package com.example.demo.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Data
@Accessors(chain = true)
public class RefreshToken implements Serializable {

	@Id
	private String token;

	private boolean remember = false;

	@NotNull
	private LocalDateTime expiryTime;

	@ManyToOne
	@JsonIgnore
	private User user;

}
