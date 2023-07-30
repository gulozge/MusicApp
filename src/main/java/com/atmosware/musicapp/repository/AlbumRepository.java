package com.atmosware.musicapp.repository;

import com.atmosware.musicapp.entities.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AlbumRepository extends JpaRepository<Album, UUID> {
    boolean existsById(UUID id);
}
