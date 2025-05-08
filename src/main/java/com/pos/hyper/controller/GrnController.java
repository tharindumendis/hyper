package com.pos.hyper.controller;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.DTO.GRNDto;
import com.pos.hyper.model.grn.GRNService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grn")
public class GrnController {

    private final GRNService grnService;
    private final CustomExceptionHandler customExceptionHandler;
    public GrnController(GRNService grnService, CustomExceptionHandler customExceptionHandler) {
        this.grnService = grnService;
        this.customExceptionHandler = customExceptionHandler;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllGRNs() {
        return grnService.getAllGRNs();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getGRNById(@PathVariable Integer id) {
        return grnService.getGRNById(id);
    }
    @PostMapping(path = "",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createGRN(@Valid @RequestBody GRNDto grnDto) {
        return grnService.createGRN(grnDto);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGRN(@PathVariable Integer id, @Valid @RequestBody GRNDto grnDto) {
        return grnService.updateGRN(id, grnDto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGRN(@PathVariable Integer id) {
        return grnService.deleteGRN(id);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exp) {
        return customExceptionHandler.handleMethodArgumentNotValid(exp);
    }

}
