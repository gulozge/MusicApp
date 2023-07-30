package com.atmosware.musicapp.business.concretes;


import com.atmosware.musicapp.business.abstracts.ArtistService;
import com.atmosware.musicapp.business.dto.requests.ArtistRequest;
import com.atmosware.musicapp.business.dto.responses.ArtistResponse;
import com.atmosware.musicapp.business.rules.ArtistBusinessRules;
import com.atmosware.musicapp.entities.Artist;
import com.atmosware.musicapp.repository.ArtistRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ArtistManager implements ArtistService {
    private final ArtistRepository repository;
    private final ArtistBusinessRules rules;


    @Override
    public List<ArtistResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(artist -> ArtistResponse.builder()
                        .id(artist.getId())
                        .firstName(artist.getFirstName())
                        .lastName(artist.getLastName())
                        .biography(artist.getBiography())
                        .build())
                .toList();
    }

    @Override
    public ArtistResponse getById(UUID id) {
        Artist artist = repository.findById(id).orElseThrow();
        return ArtistResponse.builder()
                .id(artist.getId())
                .firstName(artist.getFirstName())
                .lastName(artist.getLastName())
                .biography(artist.getBiography())
                .build();
    }

    @Override
    public ArtistResponse add(ArtistRequest request) {
        Artist artist = new Artist();
        artist.setId(null);
        artist.setFirstName(request.getFirstName());
        artist.setLastName(request.getLastName());
        artist.setBiography(request.getBiography());
        repository.save(artist);
        return ArtistResponse.builder()
                .id(artist.getId())
                .firstName(artist.getFirstName())
                .lastName(artist.getLastName())
                .biography(artist.getBiography())
                .build();

    }

    @Override
    public ArtistResponse update(UUID id, ArtistRequest request) {
        Artist artist = repository.findById(id).orElseThrow();
        artist.setId(id);
        artist.setFirstName(request.getFirstName());
        artist.setLastName(request.getLastName());
        artist.setBiography(request.getBiography());
        repository.save(artist);
        return ArtistResponse.builder()
                .id(artist.getId())
                .firstName(artist.getFirstName())
                .lastName(artist.getLastName())
                .biography(artist.getBiography())
                .build();

    }

    @Override
    public void delete(UUID id) {
        rules.checkIfArtistExists(id);
        repository.deleteById(id);
    }

    public Artist mapToArtist(ArtistResponse artistResponse) {
        Artist artist = new Artist();
        artist.setId(artistResponse.id());
        artist.setFirstName(artistResponse.firstName());
        artist.setLastName(artistResponse.lastName());
        artist.setBiography(artistResponse.biography());
        return artist;
    }

}
