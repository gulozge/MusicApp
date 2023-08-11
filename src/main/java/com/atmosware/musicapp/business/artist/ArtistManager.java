package com.atmosware.musicapp.business.artist;


import com.atmosware.musicapp.constants.Messages;
import com.atmosware.musicapp.entity.Artist;
import com.atmosware.musicapp.exception.BusinessException;
import com.atmosware.musicapp.repository.ArtistRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Slf4j
@Service
@AllArgsConstructor
public class ArtistManager implements ArtistService {
    private final ArtistRepository repository;



    @Override
    public List<ArtistResponse> getAll() {
        List<ArtistResponse> arists = repository.findAll()
                .stream()
                .map(artist -> ArtistResponse.builder()
                        .id(artist.getId())
                        .firstName(artist.getFirstName())
                        .lastName(artist.getLastName())
                        .biography(artist.getBiography())
                        .build())
                .toList();
        log.info("Found {} albums", arists.size());
        return arists;
    }

    @Override
    public ArtistResponse getById(UUID id) {
        Artist artist = repository.findById(id).orElseThrow();
        log.info("Found album with id: {}", id);
        return ArtistResponse.builder()
                .id(artist.getId())
                .firstName(artist.getFirstName())
                .lastName(artist.getLastName())
                .biography(artist.getBiography())
                .build();
    }

    @Override
    public ArtistResponse add(ArtistRequest request) {
        log.info("Adding new artist with name: {}...", request.getFirstName());
        Artist artist = new Artist();
        artist.setId(null);
        return saveArtistAndReturnResponse(artist,request);

    }

    @Override
    public ArtistResponse update(UUID id, ArtistRequest request) {
        log.info("Updating artist with id: {}...", id);
        Artist artist = repository.findById(id).orElseThrow();
        artist.setId(id);
        return saveArtistAndReturnResponse(artist,request);
    }

    @Override
    public void delete(UUID id) {
        log.warn("Deleting artist with id: {}...", id);
        checkIfArtistExists(id);
        repository.deleteById(id);
        log.info("Artist deleted successfully with id: {}", id);
    }

    public Artist mapToArtist(ArtistResponse artistResponse) {
        Artist artist = new Artist();
        artist.setId(artistResponse.id());
        artist.setFirstName(artistResponse.firstName());
        artist.setLastName(artistResponse.lastName());
        artist.setBiography(artistResponse.biography());
        return artist;
    }
    private ArtistResponse saveArtistAndReturnResponse(Artist artist, ArtistRequest request) {
        artist.setFirstName(request.getFirstName());
        artist.setLastName(request.getLastName());
        artist.setBiography(request.getBiography());
        repository.save(artist);
        log.info("{} successfully with id: {}", artist.getId() == null ? "Artist added" : "Artist updated", artist.getId());
        return ArtistResponse.builder()
                .id(artist.getId())
                .firstName(artist.getFirstName())
                .lastName(artist.getLastName())
                .biography(artist.getBiography())
                .build();
    }
    private void checkIfArtistExists(UUID id) {
        if (!repository.existsById(id)) {
            log.error("Artist not exists with id: {}", id);
            throw new BusinessException(Messages.Artist.NOT_FOUND);
        }
    }

}
