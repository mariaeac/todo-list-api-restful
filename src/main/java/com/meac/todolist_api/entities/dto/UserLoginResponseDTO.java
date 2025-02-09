package com.meac.todolist_api.entities.dto;

import java.util.UUID;

public record UserLoginResponseDTO (UUID userId, String token, String tokenType, long tokenExpiration) {
}
