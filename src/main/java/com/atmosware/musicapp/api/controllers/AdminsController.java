package com.atmosware.musicapp.api.controllers;


import com.atmosware.musicapp.business.abstracts.AdminService;
import com.atmosware.musicapp.business.dto.requests.AdminRequest;
import com.atmosware.musicapp.business.dto.responses.AdminResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admins")
public class AdminsController {
    private final AdminService service;

    @GetMapping
    public List<AdminResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public AdminResponse getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public AdminResponse add(@Valid @RequestBody AdminRequest request) {
        return service.add(request);
    }

    @PostMapping("/login")
    public void login(@Valid @RequestBody AdminRequest request) {
        service.login(request);
    }

    @PutMapping("/{id}")
    public AdminResponse update(@PathVariable UUID id, @Valid @RequestBody AdminRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable UUID id) {
        service.delete(id);
    }

}
