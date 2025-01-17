package com.example.demo.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class Team extends Timestamp {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotNull
	private String name;

	private String description;

	private int totalMembers = 0;

	private int completedProjects = 0;

	private int activeProjects = 0;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "manager_id")
	private User manager;

	@OneToMany(mappedBy = "team")
	@JsonManagedReference
	private List<User> members = new ArrayList<>();

	@OneToMany
	@JsonManagedReference
	private List<Project> projects = new ArrayList<>();

	@PrePersist
	@PreUpdate
	public void updateTotalMembers() {

		if (members != null) {

			this.totalMembers = members.size();

		} else {

			this.totalMembers = 0;

		}

	}

}
