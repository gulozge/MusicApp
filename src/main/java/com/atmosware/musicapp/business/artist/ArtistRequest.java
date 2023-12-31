package com.atmosware.musicapp.business.artist;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArtistRequest {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String biography;
}

