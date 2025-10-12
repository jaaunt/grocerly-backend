package com.stitch.grocerly.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UsersRepository extends JpaRepository<Users, Integer> {
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    Optional<Users> findByUsername(String username);

}
