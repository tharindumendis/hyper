package com.pos.hyper.controller;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.DTO.InvoiceDto;
import com.pos.hyper.model.invoice.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final CustomExceptionHandler customExceptionHandler;


    public InvoiceController(InvoiceService invoiceService, CustomExceptionHandler customexceptionHandler) {
        this.invoiceService = invoiceService;
        this.customExceptionHandler = customexceptionHandler;
    }

    @GetMapping("")
    public List<InvoiceDto> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }
    @GetMapping("/{id}")
    public InvoiceDto getInvoiceById(@PathVariable Integer id) {
        return invoiceService.getInvoiceById(id);
    }

    @PostMapping("")
    public InvoiceDto createInvoice(@RequestBody InvoiceDto invoiceDto) {
        return invoiceService.createInvoice(invoiceDto);
    }
    @PutMapping("/{id}")
    public InvoiceDto updateInvoice(@PathVariable Integer id, @RequestBody InvoiceDto invoiceDto) {
        return invoiceService.updateInvoice(id, invoiceDto);
    }
    @DeleteMapping("/{id}")
    public void deleteInvoice(@PathVariable Integer id) {
        invoiceService.deleteInvoice(id);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exp) {
        return customExceptionHandler.handleMethodArgumentNotValid(exp);
    }
}
