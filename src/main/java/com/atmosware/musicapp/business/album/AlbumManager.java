package com.atmosware.musicapp.business.album;


import com.atmosware.musicapp.business.artist.ArtistResponse;
import com.atmosware.musicapp.business.artist.ArtistService;
import com.atmosware.musicapp.constants.Messages;
import com.atmosware.musicapp.entity.Album;
import com.atmosware.musicapp.entity.Artist;
import com.atmosware.musicapp.exception.BusinessException;
import com.atmosware.musicapp.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlbumManager implements AlbumService {
    private final AlbumRepository repository;
    private final ArtistService artistService;

    @Override
    public List<AlbumResponse> getAll() {
        List<AlbumResponse> albums = repository.findAll()
                .stream()
                .map(album -> AlbumResponse
                        .builder()
                        .id(album.getId())
                        .artistId(album.getArtist().getId())
                        .name(album.getName())
                        .description(album.getDescription())
                        .releaseDate(album.getReleaseDate())
                        .build())
                .toList();
        log.info("Found {} albums", albums.size());
        return albums;
    }

    @Override
    public AlbumResponse getById(UUID id) {
        Album album = repository.findById(id).orElseThrow();
        log.info("Found album with id: {}", id);
        return AlbumResponse
                .builder()
                .id(album.getId())
                .artistId(album.getArtist().getId())
                .name(album.getName())
                .description(album.getDescription())
                .releaseDate(album.getReleaseDate())
                .build();
    }

    @Override
    public AlbumResponse add(AlbumRequest request) {
        log.info("Adding new album with artistId: {}...", request.getArtistId());
        checkIfAlbumAlreadyExists(request.getName(), request.getArtistId());
        Album album = new Album();
        album.setId(null);
        return saveAlbumAndReturnResponse(album, request);
    }

    @Override
    public AlbumResponse update(UUID id, AlbumRequest request) {
        log.info("Updating album with id: {}...", id);
        Album album = repository.findById(id).orElseThrow();
        album.setId(id);
        return saveAlbumAndReturnResponse(album, request);
    }

    @Override
    public void delete(UUID id) {
        log.warn("Deleting album with id: {}...", id);
        checkIfAlbumExists(id);
        repository.deleteById(id);
        log.info("Album deleted successfully with id: {}", id);
    }

    public Album mapToAlbum(AlbumResponse albumResponse) {
        Album album = new Album();
        ArtistResponse artist = artistService.getById(albumResponse.artistId());
        album.setId(albumResponse.id());
        album.setArtist(artistService.mapToArtist(artist));
        album.setName(albumResponse.name());
        album.setDescription(albumResponse.description());
        album.setReleaseDate(albumResponse.releaseDate());
        return album;
    }
    private AlbumResponse saveAlbumAndReturnResponse(Album album, AlbumRequest request) {
        ArtistResponse artist = artistService.getById(request.getArtistId());
        album.setArtist(artistService.mapToArtist(artist));
        album.setName(request.getName());
        album.setDescription(request.getDescription());
        album.setReleaseDate(request.getReleaseDate());
        repository.save(album);
        log.info("{} successfully with id: {}", album.getId() == null ? "Album added" : "Album updated", album.getId());
        return AlbumResponse
                .builder()
                .id(album.getId())
                .artistId(album.getArtist().getId())
                .name(album.getName())
                .description(album.getDescription())
                .releaseDate(album.getReleaseDate())
                .build();
    }
    private void checkIfAlbumExists(UUID id) {
        if (!repository.existsById(id)) {
            log.error("Album not exists with id: {}", id);
            throw new BusinessException(Messages.Album.NOT_EXISTS);
        }
    }

    private void checkIfAlbumAlreadyExists(String name, UUID artistId) {
        ArtistResponse artist = artistService.getById(artistId);
        Artist convertArtist = artistService.mapToArtist(artist);
        if (repository.existsByNameAndArtist(name, convertArtist)) {
            log.error("Album already exists: {}", name);
            throw new BusinessException(Messages.Album.EXISTS);
        }
    }
}
