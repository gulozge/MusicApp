package com.atmosware.musicapp.business.abstracts;


import com.atmosware.musicapp.business.dto.requests.AlbumRequest;
import com.atmosware.musicapp.business.dto.responses.AlbumResponse;
import com.atmosware.musicapp.entities.Album;

import java.util.List;
import java.util.UUID;

public interface AlbumService {
    List<AlbumResponse> getAll();

    AlbumResponse getById(UUID id);

    AlbumResponse add(AlbumRequest request);

    AlbumResponse update(UUID id, AlbumRequest request);

    void delete(UUID id);

    Album mapToAlbum(AlbumResponse albumResponse);

}
