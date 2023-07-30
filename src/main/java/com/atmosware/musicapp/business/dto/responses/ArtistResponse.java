package com.atmosware.musicapp.business.dto.responses;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ArtistResponse(UUID id, String firstName, String lastName, String biography) {
}
