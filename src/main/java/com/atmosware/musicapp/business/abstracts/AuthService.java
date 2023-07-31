package com.atmosware.musicapp.business.abstracts;

import com.atmosware.musicapp.business.dto.requests.LoginRequest;
import com.atmosware.musicapp.business.dto.responses.JwtAuthenticationResponse;

public interface AuthService {
    JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest);
}
