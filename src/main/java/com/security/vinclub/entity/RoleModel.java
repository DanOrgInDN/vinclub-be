package com.security.vinclub.entity;

import com.security.vinclub.repository.RoleRepository;
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
