package com.security.vinclub.repository;

import com.security.vinclub.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    String TABLE = "user";

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    @Query(value = "SELECT * FROM " + TABLE +
            " WHERE email LIKE %:email% AND CONCAT(firstName,lastName) LIKE %:username% ", nativeQuery = true)
    Page<User> findByEmailAndUsername(@Param("email") String email, @Param("username") String username, Pageable pageable);
}
