package com.atmosware.musicapp.repository;

import com.atmosware.musicapp.entity.Album;
import com.atmosware.musicapp.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AlbumRepository extends JpaRepository<Album, UUID> {
    boolean existsById(UUID id);
    boolean existsByNameAndArtist(String name, Artist artist);
}
