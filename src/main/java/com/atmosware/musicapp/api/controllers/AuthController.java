package com.atmosware.musicapp.api.controllers;

import com.atmosware.musicapp.business.abstracts.AuthService;
import com.atmosware.musicapp.business.dto.requests.LoginRequest;
import com.atmosware.musicapp.business.dto.responses.JwtAuthenticationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    public JwtAuthenticationResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return service.authenticateUser(loginRequest);
    }
}