package com.atmosware.musicapp.business.album;


import com.atmosware.musicapp.entity.Album;

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
