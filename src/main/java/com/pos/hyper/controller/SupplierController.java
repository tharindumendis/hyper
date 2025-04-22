package com.pos.hyper.controller;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.model.supplier.SupplierDto;
import com.pos.hyper.model.supplier.SupplierService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

    private final SupplierService supplierService;
    private final CustomExceptionHandler customExceptionHandler;

    public SupplierController(SupplierService supplierService, CustomExceptionHandler customExceptionHandler) {
        this.supplierService = supplierService;
        this.customExceptionHandler = customExceptionHandler;
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
                return customExceptionHandler.handleMethodArgumentNotValid(exp);
    }

}
