package com.atmosware.musicapp.business.concretes;


import com.atmosware.musicapp.business.abstracts.AlbumService;
import com.atmosware.musicapp.business.abstracts.ArtistService;
import com.atmosware.musicapp.business.abstracts.SongService;
import com.atmosware.musicapp.business.dto.requests.SongRequest;
import com.atmosware.musicapp.business.dto.responses.AlbumResponse;
import com.atmosware.musicapp.business.dto.responses.ArtistResponse;
import com.atmosware.musicapp.business.dto.responses.SongResponse;
import com.atmosware.musicapp.business.rules.AlbumBusinessRules;
import com.atmosware.musicapp.business.rules.ArtistBusinessRules;
import com.atmosware.musicapp.business.rules.SongBusinessRules;
import com.atmosware.musicapp.entities.Album;
import com.atmosware.musicapp.entities.Song;
import com.atmosware.musicapp.repository.SongRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class SongManager implements SongService {
    private final SongRepository repository;
    private final ArtistService artistService;
    private final AlbumService albumService;
    private final SongBusinessRules rules;
    private final ArtistBusinessRules artistBusinessRules;
    private final AlbumBusinessRules albumBusinessRules;
    private final RedisTemplate<String, String> redisTemplate;


    @Override
    public List<SongResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(song -> {
                    UUID albumId = getAlbumId(song);
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
    }

    @Override
    public SongResponse getById(UUID id) {
        Song song = repository.findById(id).orElseThrow();
        UUID albumId = getAlbumId(song);
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
        Song song = new Song();
        if (request.getAlbumId() == null) {
            song.setAlbum(null);
            ArtistResponse artist = artistService.getById(request.getArtistId());
            song.setArtist(artistService.mapToArtist(artist));
        } else {
            albumBusinessRules.checkIfAlbumExists(request.getAlbumId());
            artistBusinessRules.checkIfArtistExists(request.getArtistId());
            AlbumResponse album = albumService.getById(request.getAlbumId());
            song.setAlbum(albumService.mapToAlbum(album));
            ArtistResponse artist = artistService.getById(album.artistId());
            song.setArtist(artistService.mapToArtist(artist));
        }

        song.setId(null);
        song.setName(request.getName());
        song.setDescription(request.getDescription());
        song.setCategory(request.getCategory());
        song.setLyrics(request.getLyrics());
        repository.save(song);
        UUID albumId = getAlbumId(song);
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

    @Override
    public SongResponse update(UUID id, SongRequest request) {
        Song song = new Song();
        ArtistResponse artist = artistService.getById(request.getArtistId());
        AlbumResponse album = albumService.getById(request.getAlbumId());
        song.setId(id);
        song.setAlbum(albumService.mapToAlbum(album));
        song.setArtist(artistService.mapToArtist(artist));
        song.setName(request.getName());
        song.setDescription(request.getDescription());
        song.setCategory(request.getCategory());
        song.setLyrics(request.getLyrics());
        repository.save(song);
        UUID albumId = getAlbumId(song);
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

    @Override
    public void delete(UUID id) {
        rules.checkIfSongExists(id);
        repository.deleteById(id);
    }

    public Song mapToSong(SongResponse songResponse) {
        rules.checkIfSongExists(songResponse.id());
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
        return repository.findAllByAlbumId(albumId)
                .stream()
                .map(song -> {
                    UUID getAlbumId = getAlbumId(song);
                    return SongResponse.builder()
                            .id(song.getId())
                            .albumId(getAlbumId)
                            .artistId(song.getArtist().getId())
                            .name(song.getName())
                            .description(song.getDescription())
                            .category(song.getCategory())
                            .lyrics(song.getLyrics())
                            .build();
                })
                .toList();
    }

    @Override
    public List<SongResponse> getAllByArtistId(UUID artistId) {
        return repository.findAllByArtistId(artistId)
                .stream()
                .map(song -> {
                    UUID albumId = getAlbumId(song);
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
    }

    @Override
    public List<SongResponse> getAllBySongName(String songName) {
        return repository.findAllByName(songName)
                .stream()
                .map(song -> {
                    UUID albumId = getAlbumId(song);
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
    }

    @Override
    public List<SongResponse> getAllByArtistNameAndSongName(String artistName, String songName) {
        return repository.findAllByArtistFirstNameAndName(artistName, songName)
                .stream()
                .map(song -> {
                    UUID albumId = getAlbumId(song);
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
    }


    private UUID getAlbumId(Song song) {
        return Optional.ofNullable(song.getAlbum()).map(Album::getId).orElse(null);
    }

    public List<SongResponse> getMostFavoriteSongs(int limit) {
        String songFavoritesKey = "song_favorites";
        Set<ZSetOperations.TypedTuple<String>> mostFavoriteSongsWithScores =
                         redisTemplate.
                        opsForZSet().
                        reverseRangeWithScores(songFavoritesKey, 0, limit - 1);

        List<SongResponse> mostFavoriteSongs = new ArrayList<>();
        for (ZSetOperations.TypedTuple<String> tuple : mostFavoriteSongsWithScores) {
            UUID songId = UUID.fromString(tuple.getValue());
            SongResponse song = getById(songId);
            mostFavoriteSongs.add(song);
        }

        return mostFavoriteSongs;
    }

}
