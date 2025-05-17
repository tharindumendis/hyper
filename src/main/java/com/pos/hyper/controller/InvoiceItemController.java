package com.pos.hyper.controller;


import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.DTO.InvoiceItemDto;
import com.pos.hyper.model.invoice_item.InvoiceItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoice/item")
public class InvoiceItemController {
    private final InvoiceItemService invoiceItemService;
    private final CustomExceptionHandler customExceptionHandler;


    public InvoiceItemController(InvoiceItemService invoiceItemService, CustomExceptionHandler customExceptionHandler) {
        this.invoiceItemService = invoiceItemService;
        this.customExceptionHandler = customExceptionHandler;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("")
    public ResponseEntity<?> getAllInvoiceItems() {
        return invoiceItemService.getAllInvoiceItems();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getInvoiceItemById(@PathVariable Integer id) {
        return invoiceItemService.getInvoiceItemById(id);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("")
    public ResponseEntity<?> createInvoiceItem(@Valid @RequestBody InvoiceItemDto invoiceItemDto) {
        return invoiceItemService.createInvoiceItem(invoiceItemDto);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateInvoiceItem(@PathVariable Integer id, @Valid @RequestBody InvoiceItemDto invoiceItemDto) {
        return ResponseEntity.ok(invoiceItemService.updateInvoiceItem(id, invoiceItemDto));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInvoiceItem(@PathVariable Integer id) {
        return invoiceItemService.deleteInvoiceItem(id);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exp) {

        return customExceptionHandler.handleMethodArgumentNotValid(exp);
    }

}
