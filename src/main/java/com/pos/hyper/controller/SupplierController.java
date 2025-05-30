package com.pos.hyper.controller;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.DTO.SupplierDto;
import com.pos.hyper.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

    private final SupplierService supplierService;
    private final CustomExceptionHandler customExceptionHandler;

    public SupplierController(SupplierService supplierService, CustomExceptionHandler customExceptionHandler) {
        this.supplierService = supplierService;
        this.customExceptionHandler = customExceptionHandler;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("")
    public ResponseEntity<?> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getSupplierById(@PathVariable Integer id) {
        return supplierService.getSupplierById(id);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<?> createSupplier(@Valid @RequestBody SupplierDto supplierDto) {
        return supplierService.createSupplier(supplierDto);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSupplier(@PathVariable Integer id, @Valid @RequestBody SupplierDto supplierDto) {
        return supplierService.updateSupplier(id, supplierDto);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteSupplier(@PathVariable Integer id) {
        supplierService.deleteSupplier(id);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exp) {
                return customExceptionHandler.handleMethodArgumentNotValid(exp);
    }

}
