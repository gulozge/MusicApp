package com.atmosware.musicapp.repository;

import com.atmosware.musicapp.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {
    boolean existsByToken(String token);
    Token findByToken(String token);
}
