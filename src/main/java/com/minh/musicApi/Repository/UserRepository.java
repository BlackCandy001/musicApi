package com.minh.musicApi.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.minh.musicApi.Models.Entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}