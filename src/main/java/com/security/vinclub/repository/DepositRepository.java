package com.security.vinclub.repository;

import com.security.vinclub.dto.response.deposit.PendingDepositResponse;
import com.security.vinclub.entity.Deposit;
import com.security.vinclub.enumeration.AppovalStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepositRepository extends JpaRepository<Deposit, String> {
    String TABLE = "deposit";

    Page<Deposit> findByStatus(AppovalStatusEnum appovalStatusEnum, Pageable pageable);

    @Query("SELECT NEW com.security.vinclub.dto.response.deposit.PendingDepositResponse(d.id, u.id, u.username, u.phone, d.status, d.accountNumber, d.accountName, d.bankName, d.amount, d.createdDate) " +
            "FROM " + TABLE + " d JOIN user u ON u.id = d.userId WHERE d.status = 0 " +
            "AND (d.accountNumber IS NULL OR d.accountNumber LIKE  %:searchText%) " +
            "OR (d.accountName IS NULL OR d.accountName LIKE  %:searchText%) " +
            "OR (u.username IS NULL OR u.username LIKE  %:searchText%) " +
            "OR (u.phone IS NULL OR u.phone LIKE  %:searchText%) " +
            "OR (d.bankName IS NULL OR d.bankName LIKE  %:searchText%)")
    Page<PendingDepositResponse> searchPendingDeposits(String searchText, Pageable pageable);
}
