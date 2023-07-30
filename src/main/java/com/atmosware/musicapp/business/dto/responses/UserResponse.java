package com.atmosware.musicapp.business.dto.responses;


import lombok.Builder;

import java.util.UUID;


@Builder
public record UserResponse(UUID id, String firstname, String lastname, String userName, String email) {
}
