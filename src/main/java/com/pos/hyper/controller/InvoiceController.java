package com.pos.hyper.controller;

import com.pos.hyper.model.customer.Customer;
import com.pos.hyper.model.invoice.Invoice;
import com.pos.hyper.model.invoice.InvoiceDto;
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
    public ResponseEntity<Invoice> getInvoice(@PathVariable Integer id) {
        return invoiceRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<Invoice> createInvoice(@Valid @RequestBody InvoiceDto invoiceDto) {
        Invoice invoice = new Invoice();
        Customer customer = new Customer();

        customer.setId(invoiceDto.customerId());

        invoice.setId(invoiceDto.id());
        invoice.setCustomer(customer);
        invoice.setTotal(invoiceDto.total());

        Invoice savedInvoice = invoiceRepository.save(invoice);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedInvoice);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    Invoice updateInvoice(@Valid @RequestBody Invoice invoice, @PathVariable Integer id) {
        Invoice inv = invoiceRepository.findById(id).get();
        //inv.setCustomerId(invoice.getCustomerId());
        inv.setCreatedAt(invoice.getCreatedAt());
        inv.setTotal(invoice.getTotal());
        return invoiceRepository.save(inv);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteInvoice(@PathVariable Integer id) {
        invoiceRepository.deleteById(id);
    }

}
