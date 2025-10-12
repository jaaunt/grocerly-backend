package com.stitch.grocerly.repository;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UsersRepository extends JpaRepository<Users, Integer> {
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
}
