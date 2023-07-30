package com.atmosware.musicapp.business.dto.responses;

import lombok.Builder;

import java.util.UUID;

@Builder
public record GetByIdFavoriteSongs(UUID id, String artistName, String name, String description,
                                   String category, String lyrics) {
}
