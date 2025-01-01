package com.security.duanspringboot.repository;

import com.security.duanspringboot.entity.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleModel, String> {
    String TABLE = "role";
}
