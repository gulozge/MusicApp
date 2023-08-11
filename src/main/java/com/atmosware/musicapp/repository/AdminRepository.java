package com.atmosware.musicapp.repository;

import com.atmosware.musicapp.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface AdminRepository extends JpaRepository<Admin, UUID> {
    Admin findByEmail(String email);

    boolean existsByEmail(String email);


}
