package com.atmosware.musicapp.repository;

import com.atmosware.musicapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUserName(String userName);
    User findByEmail(String email);

    boolean existsByUserName(String email);


}
