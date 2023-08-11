package com.atmosware.musicapp.repository;


import com.atmosware.musicapp.entity.Artist;
import com.atmosware.musicapp.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface SongRepository extends JpaRepository<Song, UUID> {
    boolean existsById(UUID id);

    List<Song> findAllByAlbumId(UUID albumId);

    List<Song> findAllByArtistId(UUID artistId);

    List<Song> findAllByName(String songName);

    @Query("SELECT s FROM Song s Inner Join Artist a on s.artist.id" +
            "=a.id WHERE a.firstName=:artistFirstName and s.name=:songName")
    List<Song> findAllByArtistFirstNameAndName(@Param("artistFirstName") String artistFirstName, @Param("songName") String songName);

    List<Song> findByIdIn(List<UUID> popularSongIdsAsUUID);

    boolean existsByNameAndArtistIdAndAlbumId(String name, UUID artistId, UUID albumId);

}
