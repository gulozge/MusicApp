package com.atmosware.musicapp.business.admin;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AdminResponse(UUID id, String email) {
}
