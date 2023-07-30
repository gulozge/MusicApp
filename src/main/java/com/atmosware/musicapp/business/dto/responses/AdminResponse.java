package com.atmosware.musicapp.business.dto.responses;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AdminResponse(UUID id, String email) {
}
