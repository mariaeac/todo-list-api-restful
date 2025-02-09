package com.meac.todolist_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.View;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandlerGlobal extends RuntimeException {



    @ExceptionHandler({CustomizedExceptions.EmailAlreadyExistsException.class,
                        CustomizedExceptions.BadRequestException.class})
    public ResponseEntity<ErrorResponse> handleCustomizedExceptions(RuntimeException ex) {
        HttpStatus status = HttpStatus.resolve(
                ((ResponseStatusException) ex).getStatusCode().value()
        );
        ErrorResponse error = new ErrorResponse(ex.getMessage(), status.value());
        return new ResponseEntity<>(error, status);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        ErrorResponse error = new ErrorResponse("Invalid data", HttpStatus.BAD_REQUEST.value(), errors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);


    }

}
