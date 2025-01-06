package com.example.demo.requests;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.dto.ProfileDto;
import com.example.demo.models.UserPosition;
import com.example.demo.models.UserStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UserCreateUpdateRequest {

	private String name;

	private String email;

	private String password;

	private UserPosition position;

	private Long supervisorId;

	private List<Long> roleIds = new ArrayList<>();

	private Long workTimeId;

	private Long teamId;

	private ProfileDto profile;

	private UserStatus status = UserStatus.ACTIVE;

}
