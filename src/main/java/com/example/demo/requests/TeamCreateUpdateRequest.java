package com.example.demo.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class TeamCreateUpdateRequest {

	@NotNull
	private String name;

	private String description;

}
