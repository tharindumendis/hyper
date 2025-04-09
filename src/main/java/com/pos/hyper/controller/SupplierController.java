package com.pos.hyper.controller;

import com.pos.hyper.exception.GlobalExceptionHandler;
import com.pos.hyper.model.supplier.SupplierDto;
import com.pos.hyper.model.supplier.SupplierService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

    private final SupplierService supplierService;
    private final GlobalExceptionHandler globalExceptionHandler;

    public SupplierController(SupplierService supplierService, GlobalExceptionHandler globalExceptionHandler) {
        this.supplierService = supplierService;
        this.globalExceptionHandler = globalExceptionHandler;
    }


    @GetMapping("")
    public List<SupplierDto> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }
    @GetMapping("/{id}")
    public SupplierDto getSupplierById(@PathVariable Integer id) {
        return supplierService.getSupplierById(id);
    }
    @PostMapping("")
    public SupplierDto createSupplier(@Valid @RequestBody SupplierDto supplierDto) {
        return supplierService.createSupplier(supplierDto);
    }
    @PutMapping("/{id}")
    public SupplierDto updateSupplier(@PathVariable Integer id, @Valid @RequestBody SupplierDto supplierDto) {
        return supplierService.updateSupplier(id, supplierDto);
    }
    @DeleteMapping("/{id}")
    public void deleteSupplier(@PathVariable Integer id) {
        supplierService.deleteSupplier(id);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exp) {
                return globalExceptionHandler.handleMethodArgumentNotValid(exp);
    }

}
