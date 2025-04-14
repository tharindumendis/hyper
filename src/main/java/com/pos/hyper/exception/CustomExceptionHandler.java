package com.pos.hyper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {

    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exp) {
        var errors = new HashMap<String, String>();
        exp.getBindingResult().getAllErrors().forEach(error -> {
            var fieldName = ((FieldError) error).getField();
            var errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    public ResponseStatusException handleNotFoundException(String message) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, message);

    }
    public ResponseStatusException handleBadRequestException(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);

    }

    public ResponseStatusException handleBadRequestExceptionSet(List<String> errors) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join("; ", errors));
    }



}
