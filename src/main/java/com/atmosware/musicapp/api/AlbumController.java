package com.atmosware.musicapp.api;


import com.atmosware.musicapp.business.album.AlbumService;
import com.atmosware.musicapp.business.album.AlbumRequest;
import com.atmosware.musicapp.business.album.AlbumResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/albums")
public class AlbumController {
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
