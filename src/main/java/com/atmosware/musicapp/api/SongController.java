package com.atmosware.musicapp.api;


import com.atmosware.musicapp.business.song.SongService;
import com.atmosware.musicapp.business.song.SongRequest;
import com.atmosware.musicapp.business.song.SongResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/songs")
public class SongController {
    private final SongService service;

    @GetMapping
    /*@Cacheable(value = "songs", key = "'all'")*/
    public List<SongResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    /*@Cacheable(value = "songs", key = "#id")*/
    public SongResponse getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
  /*  @CachePut(value = "songs", key = "#result.id")*/
    public SongResponse add(@Valid @RequestBody SongRequest request) {
        return service.add(request);
    }

    @PutMapping("/{id}")
    /*@CachePut(value = "songs", key = "#id")*/
    public SongResponse update(@PathVariable UUID id, @Valid @RequestBody SongRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    /*@CacheEvict(value = "songs", allEntries = true)*/
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @GetMapping("/{albumId}/ListByAlbumId")
    /*@Cacheable(value = "songs", key = "#albumId")*/
    public List<SongResponse> getAllByAlbumId(@PathVariable UUID albumId) {
        return service.getAllByAlbumId(albumId);
    }

    @GetMapping("/{artistId}/ListByArtistId")
    /*@Cacheable(value = "songs", key = "#artistId")*/
    public List<SongResponse> getAllByArtistId(@PathVariable UUID artistId) {
        return service.getAllByArtistId(artistId);
    }

    @GetMapping("/{songName}/ListBySongName")
    /*@Cacheable(value = "songs", key = "#songName")*/
    public List<SongResponse> getAllBySongName(@PathVariable String songName) {
        return service.getAllBySongName(songName);
    }

    @GetMapping("/{artistName}/ListByArtistNameAndSongName/{songName}")
/*    @Cacheable(value = "songs", key = "#artistName.concat('-').concat(#songName)")*/
    public List<SongResponse> getAllByArtistNameAndSongName(@PathVariable String artistName, @PathVariable String songName) {
        return service.getAllByArtistNameAndSongName(artistName, songName);
    }
    @GetMapping("/MostFavoriteSongs/{pageNumber}/{pageSize}")
    public List<SongResponse> getMostFavoriteSongs(@PathVariable int pageNumber,@PathVariable int pageSize){
        return service.getMostFavoriteSongs(pageNumber,pageSize);
    }
}
