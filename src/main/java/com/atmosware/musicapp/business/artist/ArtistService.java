package com.atmosware.musicapp.business.artist;


import com.atmosware.musicapp.entity.Artist;

import java.util.List;
import java.util.UUID;

public interface ArtistService {
    List<ArtistResponse> getAll();

    ArtistResponse getById(UUID id);

    ArtistResponse add(ArtistRequest request);

    ArtistResponse update(UUID id, ArtistRequest request);

    void delete(UUID id);

    Artist mapToArtist(ArtistResponse artistResponse);
}
