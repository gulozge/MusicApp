package com.atmosware.musicapp.business.dto.responses;

import lombok.Builder;

import java.util.Date;
import java.util.UUID;

@Builder
public record AlbumResponse(UUID id, UUID artistId, String name, String description, Date releaseDate) {
}
