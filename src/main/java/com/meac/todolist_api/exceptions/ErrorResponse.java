package com.meac.todolist_api.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ErrorResponse(HttpStatus errorCode, String errorMessage, List<String> errorDetails) {
    public ErrorResponse(HttpStatus errorCode, String errorMessage) {
        this(errorCode, errorMessage, null);
    }
}
