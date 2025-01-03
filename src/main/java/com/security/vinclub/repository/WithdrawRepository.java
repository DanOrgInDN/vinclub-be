package com.security.vinclub.repository;

import com.security.vinclub.entity.Withdraw;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawRepository extends JpaRepository<Withdraw, String> {
    String TABLE = "withdraw";
}
