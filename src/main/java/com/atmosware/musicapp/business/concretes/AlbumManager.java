package com.atmosware.musicapp.business.concretes;


import com.atmosware.musicapp.business.abstracts.AlbumService;
import com.atmosware.musicapp.business.abstracts.ArtistService;
import com.atmosware.musicapp.business.dto.requests.AlbumRequest;
import com.atmosware.musicapp.business.dto.responses.AlbumResponse;
import com.atmosware.musicapp.business.dto.responses.ArtistResponse;
import com.atmosware.musicapp.business.rules.AlbumBusinessRules;
import com.atmosware.musicapp.entities.Album;
import com.atmosware.musicapp.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlbumManager implements AlbumService {
    private final AlbumRepository repository;
    private final ArtistService artistService;
    private final AlbumBusinessRules rules;

    @Override
    public List<AlbumResponse> getAll() {
        return repository.findAll().stream()
                .map(album -> AlbumResponse
                        .builder()
                        .id(album.getId())
                        .artistId(album.getArtist().getId())
                        .name(album.getName())
                        .description(album.getDescription())
                        .releaseDate(album.getReleaseDate())
                        .build())
                .toList();
    }

    @Override
    public AlbumResponse getById(UUID id) {
        Album album = repository.findById(id).orElseThrow();
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
        Album album = new Album();
        ArtistResponse artist = artistService.getById(request.getArtistId());
        album.setId(null);
        album.setArtist(artistService.mapToArtist(artist));
        album.setName(request.getName());
        album.setDescription(request.getDescription());
        album.setReleaseDate(request.getReleaseDate());
        repository.save(album);
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
    public AlbumResponse update(UUID id, AlbumRequest request) {
        Album album = new Album();
        ArtistResponse artist = artistService.getById(request.getArtistId());
        album.setId(id);
        album.setArtist(artistService.mapToArtist(artist));
        album.setName(request.getName());
        album.setDescription(album.getDescription());
        album.setReleaseDate(request.getReleaseDate());
        repository.save(album);
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
    public void delete(UUID id) {
        rules.checkIfAlbumExists(id);
        repository.deleteById(id);
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
}
