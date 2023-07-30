package com.atmosware.musicapp.business.abstracts;


import com.atmosware.musicapp.business.dto.requests.ArtistRequest;
import com.atmosware.musicapp.business.dto.responses.ArtistResponse;
import com.atmosware.musicapp.entities.Artist;

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
