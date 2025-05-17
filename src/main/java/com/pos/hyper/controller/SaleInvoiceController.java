package com.pos.hyper.controller;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.DTO.SaleInvoiceDto;
import com.pos.hyper.service.SaleInvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sale")
public class SaleInvoiceController {

    private final SaleInvoiceService saleInvoiceService;
    private final CustomExceptionHandler customExceptionHandler;

    public SaleInvoiceController(SaleInvoiceService saleInvoiceService, CustomExceptionHandler customExceptionHandler) {
        this.saleInvoiceService = saleInvoiceService;
        this.customExceptionHandler = customExceptionHandler;
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("")
    public ResponseEntity<?> getSale() {
        return saleInvoiceService.getSale();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getSaleById(@PathVariable Integer id) {
       return saleInvoiceService.getSaleById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("")
    public ResponseEntity<?> createSale(@RequestBody SaleInvoiceDto saleInvoiceDto) {
        return saleInvoiceService.createSale(saleInvoiceDto);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> returnSale(@PathVariable Integer id, @RequestBody SaleInvoiceDto saleInvoiceDto) {
        return saleInvoiceService.returnSale(id, saleInvoiceDto);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exp) {
        return customExceptionHandler.handleMethodArgumentNotValid(exp);
    }




}
