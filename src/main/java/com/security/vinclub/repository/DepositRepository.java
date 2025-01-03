package com.security.vinclub.repository;

import com.security.vinclub.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositRepository extends JpaRepository<Deposit, String> {
    String TABLE = "deposit";
}
