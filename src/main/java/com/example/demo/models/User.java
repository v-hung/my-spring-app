package com.example.demo.models;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	private int id;

	private String name;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	@JsonIgnore
	private String password;

	@Enumerated(EnumType.STRING)
	private UserPosition userPosition;

	@OneToMany(cascade = CascadeType.ALL)
	private Set<TimeSheet> timeSheets = new HashSet<>();

	@ManyToMany
	private Set<Role> roles = new HashSet<>();

	@OneToMany
	private Set<RefreshToken> refreshTokens = new HashSet<>();

	@ManyToOne
	private WorkTime workTime;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

		if (roles.stream().anyMatch(Role::isAdmin)) {

			grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));

			return grantedAuthorities;

		}

		for (Role role : roles) {

			for (Permission permission : role.getPermissions()) {

				grantedAuthorities.add(new SimpleGrantedAuthority(permission.getName()));

			}

		}

		return grantedAuthorities;

	}

	@Override
	public boolean isEnabled() {

		return UserDetails.super.isEnabled();

	}
}
