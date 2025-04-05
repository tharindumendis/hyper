package com.pos.hyper.controller;

import com.pos.hyper.model.Invoice;
import com.pos.hyper.repository.InvoiceRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @GetMapping("")
    List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }
    @PostMapping("")
    Invoice createInvoice(@Valid @RequestBody Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @PutMapping("/{id}")
    Invoice updateInvoice(@Valid @RequestBody Invoice invoice, @PathVariable Long id) {
        Invoice inv = invoiceRepository.findById(id).get();
        inv.setCustomerId(invoice.getCustomerId());
        inv.setInvoiceDate(invoice.getInvoiceDate());
        inv.setTotal(invoice.getTotal());
        return invoiceRepository.save(inv);
    }

    @DeleteMapping("/{id}")
    void deleteInvoice(@PathVariable Long id) {
        invoiceRepository.deleteById(id);
    }

}
