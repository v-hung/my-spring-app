package com.example.demo.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String name;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	@JsonIgnore
	private String password;

	@Enumerated(EnumType.STRING)
	private UserPosition position;

	@ManyToOne
	@JsonIgnoreProperties({ "supervisor", "roles", "refreshTokens", "workTime", "team" })
	private User supervisor;

	@ManyToMany
	@Column(nullable = false)
	private Set<Role> roles = new HashSet<>();

	@OneToMany
	@JsonIgnore
	private List<RefreshToken> refreshTokens = new ArrayList<>();

	@ManyToOne
	private WorkTime workTime;

	@ManyToOne
	@JsonBackReference
	private Team team;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "profile_id", referencedColumnName = "id")
	@JsonBackReference
	private Profile profile;

	@NotNull
	private boolean isFirstLogin = true;

	@NotNull
	@Enumerated(EnumType.STRING)
	private UserStatus status = UserStatus.ACTIVE;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

		if (roles.stream().anyMatch(Role::isAdmin)) {

			grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));

			return grantedAuthorities;

		}

		for (Role role : roles) {

			for (Permission permission : role.getPermissions()) {

				grantedAuthorities.add(new SimpleGrantedAuthority(permission.getName().name()));

			}

		}

		return grantedAuthorities;

	}

	public int getCurrentLevel() {

		return roles.stream().map(Role::getLevel).max(Comparator.naturalOrder()).orElse(1);

	}

	@Override
	public boolean isEnabled() {

		UserStatus[] statusInActive = {
			UserStatus.INACTIVE,
			UserStatus.OFF_BOARDING
		};

		return Arrays.asList(statusInActive).contains(status);

	}
}
