package com.atmosware.musicapp.business.dto.responses;

import lombok.Builder;

import java.util.UUID;

@Builder
public record
SongResponse(UUID id, UUID albumId, UUID artistId, String name, String description, String category, String lyrics) {
}
