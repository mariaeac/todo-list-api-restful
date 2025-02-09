package com.meac.todolist_api.entities.dto;
import jakarta.validation.constraints.NotBlank;

public record UserRegisterDTO(
        @NotBlank String name,
        @NotBlank String email,
        @NotBlank String password ) {

}
