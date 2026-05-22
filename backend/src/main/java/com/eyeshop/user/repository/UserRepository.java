package com.eyeshop.user.repository;

import com.eyeshop.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    // For Login: find user through email
    Optional<User> findByEmail(String email);

    // For Registration: check if email already exists
    boolean existsByEmail(String email);
}

