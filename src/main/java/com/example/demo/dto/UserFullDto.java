package com.example.demo.dto;

import com.example.demo.models.WorkTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFullDto extends UserDto {

	private ProfileDto profile;

	private WorkTime workTime;

}
