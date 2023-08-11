package com.atmosware.musicapp.business.song;


import com.atmosware.musicapp.business.album.AlbumResponse;
import com.atmosware.musicapp.business.album.AlbumService;
import com.atmosware.musicapp.business.artist.ArtistResponse;
import com.atmosware.musicapp.business.artist.ArtistService;
import com.atmosware.musicapp.constants.Messages;
import com.atmosware.musicapp.entity.Album;
import com.atmosware.musicapp.entity.Song;
import com.atmosware.musicapp.exception.BusinessException;
import com.atmosware.musicapp.repository.SongRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.springframework.beans.factory.support.InstanceSupplier.using;

@Slf4j
@Service
@AllArgsConstructor
public class SongManager implements SongService {
    private final SongRepository repository;
    private final ArtistService artistService;
    private final AlbumService albumService;
    private final RedisTemplate<String, String> redisTemplate;


    @Override
    public List<SongResponse> getAll() {
        List<SongResponse> songs = repository.findAll()
                .stream()
                .map(song -> {
                    UUID albumId = getByAlbumId(song);
                    return SongResponse.builder()
                            .id(song.getId())
                            .albumId(albumId)
                            .artistId(song.getArtist().getId())
                            .name(song.getName())
                            .description(song.getDescription())
                            .category(song.getCategory())
                            .lyrics(song.getLyrics())
                            .build();
                })
                .toList();
        log.info("Found {} songs", songs.size());
        return songs;
    }

    @Override
    public SongResponse getById(UUID id) {
        Song song = repository.findById(id).orElseThrow();
        UUID albumId = getByAlbumId(song);
        log.info("Found song with id: {}", id);
        return SongResponse
                .builder()
                .id(song.getId())
                .albumId(albumId)
                .artistId(song.getArtist().getId())
                .name(song.getName())
                .description(song.getDescription())
                .category(song.getCategory())
                .lyrics(song.getLyrics())
                .build();
    }

    @Override
    public SongResponse add(SongRequest request) {
        log.info("Adding new song with artistId: {}...", request.getArtistId());
        Song song = new Song();
        song.setId(null);
        return saveSongAndReturnResponse(song, request);
    }

    @Override
    public SongResponse update(UUID id, SongRequest request) {
        log.info("Updating song with id: {}...", id);
        Song song = new Song();
        song.setId(id);
        return saveSongAndReturnResponse(song, request);
    }

    @Override
    public void delete(UUID id) {
        log.warn("Deleting song with id: {}...", id);
        checkIfSongExists(id);
        repository.deleteById(id);
        log.info("Song deleted successfully with id: {}", id);
    }

    @Override
    public Song mapToSong(SongResponse songResponse) {
        checkIfSongExists(songResponse.id());
        Song song = new Song();
        if (songResponse.albumId() == null) {
            song.setAlbum(null);
        } else {
            AlbumResponse album = albumService.getById(songResponse.albumId());
            song.setAlbum(albumService.mapToAlbum(album));
        }

        ArtistResponse artist = artistService.getById(songResponse.artistId());
        song.setId(songResponse.id());
        song.setArtist(artistService.mapToArtist(artist));
        song.setName(songResponse.name());
        song.setDescription(songResponse.description());
        song.setCategory(songResponse.category());
        song.setLyrics(songResponse.lyrics());
        return song;
    }

    @Override
    public List<SongResponse> getAllByAlbumId(UUID albumId) {
        log.info("Found song with albumId: {}", albumId);
        return returnSongResponse(repository.findAllByAlbumId(albumId));

    }

    @Override
    public List<SongResponse> getAllByArtistId(UUID artistId) {
        log.info("Found song with artistId: {}", artistId);
        return returnSongResponse(repository.findAllByArtistId(artistId));
    }

    @Override
    public List<SongResponse> getAllBySongName(String songName) {
        log.info("Found song with songName: {}", songName);
        return returnSongResponse(repository.findAllByName(songName));
    }

    @Override
    public List<SongResponse> getAllByArtistNameAndSongName(String artistName, String songName) {
        log.info("Found song with artistNameAndSongName: {}", artistName + songName);
        return returnSongResponse(repository.findAllByArtistFirstNameAndName(artistName, songName));
    }

    @Override
    public List<SongResponse> getMostFavoriteSongs(int pageNumber, int pageSize) {
        log.info("Fetching songs from page {} with page size {}.", pageNumber, pageSize);

        String songFavoritesKey = "song_favorites";
        int start = pageNumber * pageSize;
        int end = start + pageSize - 1;

        Set<ZSetOperations.TypedTuple<String>> mostFavoriteSongsWithScores =
                redisTemplate
                        .opsForZSet()
                        .reverseRangeWithScores(songFavoritesKey, start, end);

        if (mostFavoriteSongsWithScores==null || mostFavoriteSongsWithScores.isEmpty()) {
            log.warn("No songs found in the specified range.");
            return Collections.emptyList();
        }
        List<SongResponse> mostFavoriteSongs = new ArrayList<>();
        for (ZSetOperations.TypedTuple<String> tuple : mostFavoriteSongsWithScores) {
            UUID songId = UUID.fromString(tuple.getValue());
            SongResponse song = getById(songId);
            if(song != null) {
                mostFavoriteSongs.add(song);
            } else {
                log.warn("No song found for id: {}", songId);
            }
        }
        log.info("Successfully fetched {} songs from page {} with page size {}.", mostFavoriteSongs.size(), pageNumber, pageSize);
        return mostFavoriteSongs;
    }

    @Override
    public Song SongGetById(UUID id) {
        return repository.findById(id).orElseThrow();


    }
    private SongResponse saveSongAndReturnResponse(Song song, SongRequest request) {
        checkIfSongAlreadyExists(request);
        if (request.getAlbumId() == null) {
            song.setAlbum(null);
            ArtistResponse artist = artistService.getById(request.getArtistId());
            song.setArtist(artistService.mapToArtist(artist));
        } else {
            AlbumResponse album = albumService.getById(request.getAlbumId());
            song.setAlbum(albumService.mapToAlbum(album));
            ArtistResponse artist = artistService.getById(album.artistId());
            song.setArtist(artistService.mapToArtist(artist));
        }

        song.setName(request.getName());
        song.setDescription(request.getDescription());
        song.setCategory(request.getCategory());
        song.setLyrics(request.getLyrics());
        repository.save(song);
        UUID albumId = getByAlbumId(song);
        log.info("{} successfully with id: {}", song.getId() == null ? "Song added" : "Song updated", song.getId());
        return SongResponse.builder()
                .id(song.getId())
                .albumId(albumId)
                .artistId(song.getArtist().getId())
                .name(song.getName())
                .description(song.getDescription())
                .category(song.getCategory())
                .lyrics(song.getLyrics())
                .build();
    }

    private UUID getByAlbumId(Song song) {
        return Optional.ofNullable(song.getAlbum()).map(Album::getId).orElse(null);
    }

    private List<SongResponse> returnSongResponse(List<Song> songs) {
        return songs.stream()
                .map(this::mapToSongResponse)
                .toList();
    }

    private SongResponse mapToSongResponse(Song song) {
        UUID getAlbumId = getByAlbumId(song);
        return SongResponse.builder()
                .id(song.getId())
                .albumId(getAlbumId)
                .artistId(song.getArtist().getId())
                .name(song.getName())
                .description(song.getDescription())
                .category(song.getCategory())
                .lyrics(song.getLyrics())
                .build();
    }

    private void checkIfSongExists(UUID id) {
        if (!repository.existsById(id)) {
            log.error("Song not exists with id: {}", id);
            throw new BusinessException(Messages.Song.NOT_EXISTS);

        }
    }
    private void checkIfSongAlreadyExists(SongRequest request) {
        if (repository.existsByNameAndArtistIdAndAlbumId(request.getName(),request.getArtistId(),request.getAlbumId())) {
            throw new BusinessException(Messages.Song.EXISTS);

        }
    }


}
