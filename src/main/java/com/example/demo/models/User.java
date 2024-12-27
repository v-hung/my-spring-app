package com.example.demo.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
	@JsonIdentityReference(alwaysAsId = true)
	private User supervisor;

	@ManyToMany
	@Column(nullable = false)
	@Fetch(FetchMode.JOIN)
	private List<Role> roles = new ArrayList<>();

	@OneToMany
	@JsonIgnore
	private List<RefreshToken> refreshTokens = new ArrayList<>();

	@ManyToOne
	private WorkTime workTime;

	@ManyToMany
	@JsonIgnoreProperties("teams")
	private List<Team> teams = new ArrayList<>();

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

		return true;

	}
}
