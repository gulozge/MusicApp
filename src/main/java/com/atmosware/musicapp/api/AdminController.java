package com.atmosware.musicapp.api;


import com.atmosware.musicapp.business.admin.AdminService;
import com.atmosware.musicapp.business.admin.AdminRequest;
import com.atmosware.musicapp.business.admin.AdminResponse;
import com.atmosware.musicapp.business.admin.LoginResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admins")
public class AdminController {
    private final AdminService service;

    @GetMapping
    public List<AdminResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public AdminResponse getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping("/registion")
    @ResponseStatus(HttpStatus.CREATED)
    public AdminResponse add(@Valid @RequestBody AdminRequest request) {
        return service.add(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody AdminRequest request) {
        return service.login(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("#id.equals(authentication.principal.id)")
    public AdminResponse update(@PathVariable UUID id, @Valid @RequestBody AdminRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("#id.equals(authentication.principal.id)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable UUID id) {
        service.delete(id);
    }

}
