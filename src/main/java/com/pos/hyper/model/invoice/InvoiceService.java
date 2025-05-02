package com.pos.hyper.model.invoice;

import com.pos.hyper.DTO.InvoiceDto;
import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.model.PaymentMethod;
import com.pos.hyper.repository.InvoiceRepository;
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

    public InvoiceDto getInvoiceById(Integer id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> customExceptionHandler.handleNotFoundException("Invoice with id " + id + " not found"));
        return invoiceMapper.toInvoiceDto(invoice);
    }
    public List<InvoiceDto> getAllInvoices() {
        List<Invoice> invoices = invoiceRepository.findAll();
        return invoices.stream().map(invoiceMapper::toInvoiceDto).toList();
    }
    public InvoiceDto createInvoice(InvoiceDto invoiceDto) {

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
        return invoiceMapper.toInvoiceDto(savedInvoice);
    }
    @Transactional
    public InvoiceDto updateInvoice(Integer id, InvoiceDto invoiceDto) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> customExceptionHandler
                        .handleNotFoundException("Invoice with id " + id + " not found"));
        double total = invoice.getTotal();
        Invoice newInvoice = invoiceMapper.toInvoice(invoiceDto, invoice);
        newInvoice.setTotal(total);
        Invoice savedInvoice = invoiceRepository.save(newInvoice);
        return invoiceMapper.toInvoiceDto(savedInvoice);
    }

    public void deleteInvoice(Integer id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> customExceptionHandler.handleNotFoundException("Invoice with id " + id + " not found"));
        invoiceRepository.delete(invoice);
    }
}
