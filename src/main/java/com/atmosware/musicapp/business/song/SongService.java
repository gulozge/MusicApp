package com.atmosware.musicapp.business.song;


import com.atmosware.musicapp.entity.Song;

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
    public List<SongResponse> getMostFavoriteSongs(int pageNumber, int pageSize);
    Song SongGetById(UUID id);

}
