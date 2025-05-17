package com.pos.hyper.controller;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.DTO.InvoiceDto;
import com.pos.hyper.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final CustomExceptionHandler customExceptionHandler;


    public InvoiceController(InvoiceService invoiceService, CustomExceptionHandler customexceptionHandler) {
        this.invoiceService = invoiceService;
        this.customExceptionHandler = customexceptionHandler;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("")
    public ResponseEntity<?> getAllInvoices() {
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getInvoiceById(@PathVariable Integer id) {

        return invoiceService.getInvoiceById(id);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("")
    public ResponseEntity<?> createInvoice(@RequestBody InvoiceDto invoiceDto) {
        return invoiceService.createInvoice(invoiceDto);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateInvoice(@PathVariable Integer id, @RequestBody InvoiceDto invoiceDto) {
        return invoiceService.updateInvoice(id, invoiceDto);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInvoice(@PathVariable Integer id) {
        return invoiceService.deleteInvoice(id);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exp) {
        return customExceptionHandler.handleMethodArgumentNotValid(exp);
    }
}
