package com.atmosware.musicapp.business.abstracts;


import com.atmosware.musicapp.business.dto.requests.SongRequest;
import com.atmosware.musicapp.business.dto.responses.SongResponse;
import com.atmosware.musicapp.entities.Song;

import java.util.List;
import java.util.UUID;

public interface SongService {
    List<SongResponse> getAll();

    SongResponse getById(UUID id);

    SongResponse add(SongRequest request);

    SongResponse update(UUID id, SongRequest request);

    void delete(UUID id);

    Song mapToSong(SongResponse songResponse);

    List<SongResponse> getAllByAlbumId(UUID albumId);

    List<SongResponse> getAllByArtistId(UUID artistId);

    List<SongResponse> getAllBySongName(String songName);

    List<SongResponse> getAllByArtistNameAndSongName(String artistName, String songName);

}
