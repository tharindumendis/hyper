package com.pos.hyper.controller;

import com.pos.hyper.model.Invoice;
import com.pos.hyper.repository.InvoiceRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {


    private final InvoiceRepository invoiceRepository;

    public InvoiceController(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @GetMapping("")
    List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable Long id) {
        return invoiceRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<Invoice> createInvoice(@Valid @RequestBody Invoice invoice) {
        Invoice savedInvoice = invoiceRepository.save(invoice);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedInvoice);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    Invoice updateInvoice(@Valid @RequestBody Invoice invoice, @PathVariable Long id) {
        Invoice inv = invoiceRepository.findById(id).get();
        inv.setCustomerId(invoice.getCustomerId());
        inv.setInvoiceDate(invoice.getInvoiceDate());
        inv.setTotal(invoice.getTotal());
        return invoiceRepository.save(inv);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteInvoice(@PathVariable Long id) {
        invoiceRepository.deleteById(id);
    }

}
