package com.security.vinclub.repository;

import com.security.vinclub.entity.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleModel, String> {
    String TABLE = "role";
}
