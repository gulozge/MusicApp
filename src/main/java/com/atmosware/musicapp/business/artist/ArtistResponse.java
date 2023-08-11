package com.atmosware.musicapp.business.artist;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ArtistResponse(UUID id, String firstName, String lastName, String biography) {
}
