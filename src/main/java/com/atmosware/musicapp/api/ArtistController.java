package com.atmosware.musicapp.api;


import com.atmosware.musicapp.business.artist.ArtistService;
import com.atmosware.musicapp.business.artist.ArtistRequest;
import com.atmosware.musicapp.business.artist.ArtistResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/artists")
public class ArtistController {
    private final ArtistService service;

    @GetMapping
    public List<ArtistResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ArtistResponse getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ArtistResponse add(@Valid @RequestBody ArtistRequest request) {
        return service.add(request);
    }

    @PutMapping("/{id}")
    public ArtistResponse update(@PathVariable UUID id, @Valid @RequestBody ArtistRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}

