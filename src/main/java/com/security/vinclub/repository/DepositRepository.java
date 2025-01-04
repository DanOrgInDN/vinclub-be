package com.security.vinclub.repository;

import com.security.vinclub.entity.Deposit;
import com.security.vinclub.enumeration.AppovalStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepositRepository extends JpaRepository<Deposit, String> {
    String TABLE = "deposit";

    Page<Deposit> findByStatus(AppovalStatusEnum appovalStatusEnum, Pageable pageable);
}
