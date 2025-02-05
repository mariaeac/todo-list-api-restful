package com.meac.todolist_api.entities.dto;

import jakarta.validation.constraints.NotBlank;

public record NewTaskRequestDTO(
        @NotBlank String title,
        @NotBlank String description) {
}
