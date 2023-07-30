package com.atmosware.musicapp.business.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class AlbumRequest {
    private UUID artistId;
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private Date releaseDate;
}
