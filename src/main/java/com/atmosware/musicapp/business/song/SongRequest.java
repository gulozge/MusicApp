package com.atmosware.musicapp.business.song;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SongRequest {
    private UUID albumId;
    private UUID artistId;
    @NotBlank
    private String name;
    private String description;
    @NotBlank
    private String category;
    private String lyrics;

}


