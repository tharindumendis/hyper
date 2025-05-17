package com.pos.hyper.service;

import com.pos.hyper.DTO.InvoiceDto;
import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.model.PaymentMethod;
import com.pos.hyper.model.invoice.Invoice;
import com.pos.hyper.model.invoice.InvoiceMapper;
import com.pos.hyper.repository.InvoiceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InvoiceService {

    final InvoiceRepository invoiceRepository;
    final InvoiceMapper invoiceMapper;
    private final CustomExceptionHandler customExceptionHandler;

    public InvoiceService(InvoiceRepository invoiceRepository, InvoiceMapper invoiceMapper, CustomExceptionHandler customExceptionHandler) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
        this.customExceptionHandler = customExceptionHandler;
    }

    public ResponseEntity<?> getInvoiceById(Integer id) {
        Invoice invoice = invoiceRepository.findById(id).orElse(null);
        if (invoice == null) {
            return customExceptionHandler.notFoundException("Invoice with id " + id + " not found");
        }
        return ResponseEntity.ok(invoiceMapper.toInvoiceDto(invoice));
    }
    public List<InvoiceDto> getAllInvoices() {
        List<Invoice> invoices = invoiceRepository.findAll();
        return invoices.stream().map(invoiceMapper::toInvoiceDto).toList();
    }
    public  ResponseEntity<?> createInvoice(InvoiceDto invoiceDto) {

        Invoice invoice = invoiceMapper.toInvoice(invoiceDto);
        invoice.setTotal(0.0);


        if(invoiceDto.paymentMethod() == null){
            invoice.setPaymentMethod(PaymentMethod.CASH);
        }else {
            try {
                invoice.setPaymentMethod(PaymentMethod.valueOf(invoiceDto.paymentMethod()));
            }catch (IllegalArgumentException e){
                throw customExceptionHandler.handleBadRequestException("Invalid payment method: " + invoiceDto.paymentMethod());
            }
        }


        Invoice savedInvoice = invoiceRepository.save(invoice);
        return ResponseEntity.ok(invoiceMapper.toInvoiceDto(savedInvoice));
    }
    @Transactional
    public ResponseEntity<?> updateInvoice(Integer id, InvoiceDto invoiceDto) {
        Invoice invoice = invoiceRepository.findById(id).orElse(null);
        if(invoice == null){
            return customExceptionHandler.notFoundException("Invoice with id " + id + " not found");
        }
        double total = invoice.getTotal();
        Invoice newInvoice = invoiceMapper.toInvoice(invoiceDto, invoice);
        newInvoice.setTotal(total);
        Invoice savedInvoice = invoiceRepository.save(newInvoice);
        return ResponseEntity.ok(invoiceMapper.toInvoiceDto(savedInvoice));
    }

    public ResponseEntity<?> deleteInvoice(Integer id) {
        Invoice invoice = invoiceRepository.findById(id).orElse(null);
        if(invoice == null) {
            return customExceptionHandler.notFoundException("Invoice with id " + id + " not found");
        }
        invoiceRepository.delete(invoice);
        return ResponseEntity.ok("Invoice deleted");
    }
}
