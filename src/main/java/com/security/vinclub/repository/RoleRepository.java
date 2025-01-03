package com.security.vinclub.repository;

import com.security.vinclub.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
    String TABLE = "role";
}
