package com.atmosware.musicapp.business.song;

import lombok.Builder;

import java.util.UUID;

@Builder
public record GetByUserIdFavoriteSongs(UUID id, String artistName, String name, String description,
                                       String category, String lyrics) {
}
