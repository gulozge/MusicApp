package com.atmosware.musicapp.api.controllers;


import com.atmosware.musicapp.business.abstracts.AlbumService;
import com.atmosware.musicapp.business.dto.requests.AlbumRequest;
import com.atmosware.musicapp.business.dto.responses.AlbumResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/albums")
public class AlbumsController {
    private final AlbumService service;

    @GetMapping
    public List<AlbumResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public AlbumResponse getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public AlbumResponse add(@Valid @RequestBody AlbumRequest request) {
        return service.add(request);
    }

    @PutMapping("/{id}")
    public AlbumResponse update(@PathVariable UUID id, @Valid @RequestBody AlbumRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable UUID id) {
        service.delete(id);
    }

}
