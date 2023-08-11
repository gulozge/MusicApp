package com.atmosware.musicapp.repository;

import com.atmosware.musicapp.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ArtistRepository extends JpaRepository<Artist, UUID> {
    boolean existsById(UUID id);
}
