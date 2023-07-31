package com.atmosware.musicapp.business.dto.responses;

import lombok.Builder;

import java.util.UUID;

@Builder
public record JwtAuthenticationResponse(String accessToken, String tokenType, UUID userId, String username) {
}