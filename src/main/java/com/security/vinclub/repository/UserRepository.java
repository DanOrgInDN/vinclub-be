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

    Optional<User> findByPhone(String phone);

    Optional<User> findByUsername(String username);
    boolean existsById(String id);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phoneNumber);

    @Query(value = "SELECT * FROM " + TABLE +
            " WHERE email LIKE %:email% AND CONCAT(firstName,lastName) LIKE %:username% ", nativeQuery = true)
    Page<User> findByEmailAndUsername(@Param("email") String email, @Param("username") String username, Pageable pageable);

    @Query("SELECT u FROM " + TABLE + " u WHERE u.deleted = FALSE ")
    Page<User> findAllUsers(Pageable pageable);
}
