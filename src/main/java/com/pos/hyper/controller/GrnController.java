package com.pos.hyper.controller;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.DTO.GRNDto;
import com.pos.hyper.service.GRNService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/grn")
public class GrnController {

    private final GRNService grnService;
    private final CustomExceptionHandler customExceptionHandler;
    public GrnController(GRNService grnService, CustomExceptionHandler customExceptionHandler) {
        this.grnService = grnService;
        this.customExceptionHandler = customExceptionHandler;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ResponseEntity<?> getAllGRNs() {
        return grnService.getAllGRNs();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getGRNById(@PathVariable Integer id) {
        return grnService.getGRNById(id);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createGRN(@Valid @RequestBody GRNDto grnDto) {
        return grnService.createGRN(grnDto);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGRN(@PathVariable Integer id, @Valid @RequestBody GRNDto grnDto) {
        return grnService.updateGRN(id, grnDto);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGRN(@PathVariable Integer id) {
        return grnService.deleteGRN(id);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exp) {
        return customExceptionHandler.handleMethodArgumentNotValid(exp);
    }

}
