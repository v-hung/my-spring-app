package com.example.demo.models;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class Profile implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private LocalDate birthDate;

	private String phone;

	private boolean gender;

	private String avatar;

	private String permanentAddress;

	private String contactAddress;

	private int yearsOfWork = 0;

	@OneToOne(mappedBy = "profile")
	private User user;

}
