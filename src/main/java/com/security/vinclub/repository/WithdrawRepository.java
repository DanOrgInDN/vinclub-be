package com.security.vinclub.repository;

import com.security.vinclub.entity.Withdraw;
import com.security.vinclub.enumeration.AppovalStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WithdrawRepository extends JpaRepository<Withdraw, String> {
    String TABLE = "withdraw";

    Page<Withdraw> findByStatus(AppovalStatusEnum appovalStatusEnum, Pageable pageable);
}
