package com.atmosware.musicapp.api.controllers;


import com.atmosware.musicapp.business.abstracts.ArtistService;
import com.atmosware.musicapp.business.dto.requests.ArtistRequest;
import com.atmosware.musicapp.business.dto.responses.ArtistResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/artists")
public class ArtistsController {
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ArtistResponse add(@Valid @RequestBody ArtistRequest request) {
        return service.add(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ArtistResponse update(@PathVariable UUID id, @Valid @RequestBody ArtistRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}

