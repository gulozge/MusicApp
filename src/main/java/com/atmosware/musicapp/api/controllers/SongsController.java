package com.atmosware.musicapp.api.controllers;


import com.atmosware.musicapp.business.abstracts.SongService;
import com.atmosware.musicapp.business.dto.requests.SongRequest;
import com.atmosware.musicapp.business.dto.responses.SongResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/songs")
public class SongsController {
    private final SongService service;

    @GetMapping
    public List<SongResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public SongResponse getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public SongResponse add(@Valid @RequestBody SongRequest request) {
        return service.add(request);
    }

    @PutMapping("/{id}")
    public SongResponse update(@PathVariable UUID id, @Valid @RequestBody SongRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @GetMapping("/{albumId}/ListByAlbumId")
    public List<SongResponse> getAllByAlbumId(@PathVariable UUID albumId) {
        return service.getAllByAlbumId(albumId);
    }

    @GetMapping("/{artistId}/ListByArtistId")
    public List<SongResponse> getAllByArtistId(@PathVariable UUID artistId) {
        return service.getAllByArtistId(artistId);
    }

    @GetMapping("/{songName}/ListBySongName")
    public List<SongResponse> getAllBySongName(@PathVariable String songName) {
        return service.getAllBySongName(songName);
    }

    @GetMapping("/{artistName}/ListByArtistNameAndSongName/{songName}")
    public List<SongResponse> getAllByArtistNameAndSongName(@PathVariable String artistName, @PathVariable String songName) {
        return service.getAllByArtistNameAndSongName(artistName, songName);
    }


}
