package com.pos.hyper.exception;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
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

    public ResponseStatusException  handleNotFoundException(String message) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        return new ResponseStatusException(HttpStatus.NOT_FOUND, message);

    }
    public ResponseStatusException handleBadRequestException(String message) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);

    }

    public ResponseStatusException handleBadRequestExceptionSet(List<String> errors) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join("; ", errors));
    }
    public ResponseEntity<?> notFoundException(String message) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(new HashMap<String, String>() {{ put("error", message); }});
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Collections.singletonMap("error", message));
    }
    public ResponseEntity<?> badRequestException(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new HashMap<String, String>() {{ put("error", message); }});
    }
    public ResponseEntity<?> badRequestExceptionSet(List<String> errors) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new HashMap<String, String>() {{ put("error", String.join("; ", errors)); }});
    }

}
