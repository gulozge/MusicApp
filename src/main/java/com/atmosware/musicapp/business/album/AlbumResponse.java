package com.atmosware.musicapp.business.album;

import lombok.Builder;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Builder
public record AlbumResponse(UUID id, UUID artistId, String name, String description, LocalDate releaseDate) {
}
