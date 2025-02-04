package com.meac.todolist_api.entities.dto;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequestDTO(
        @NotBlank String email,
       @NotBlank String password) {
}
