package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

	Optional<Permission> findByName(String name);

}
