package com.security.duanspringboot.entity;

import com.security.duanspringboot.repository.RoleRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = RoleRepository.TABLE)
public class RoleModel {
    @Id
    @Column(nullable = false)
    private String roleId;
    private String roles;
}
