package com.devjoint.librarymanagement.repository;

import com.devjoint.librarymanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    
}
