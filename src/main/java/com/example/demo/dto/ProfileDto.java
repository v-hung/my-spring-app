package com.example.demo.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDto {

	private LocalDate birthDate;

	private String phone;

	private boolean gender;

	private String avatar;

	private String permanentAddress;

	private String contactAddress;

}
