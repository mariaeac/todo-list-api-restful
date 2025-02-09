package com.meac.todolist_api.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ErrorResponse(
        String message,
        int status,
        List<String> details
) {
    public ErrorResponse(String message, int status) {
        this(message, status, null);
    }
}
