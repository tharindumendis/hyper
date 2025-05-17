package com.pos.hyper.service;

import com.pos.hyper.DTO.SaleInvoiceDto;
import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.model.invoice_item.InvoiceItem;
import com.pos.hyper.model.invoice_item.InvoiceItemMapper;
import com.pos.hyper.model.invoice_item.InvoiceItemService;
import com.pos.hyper.model.invoice.Invoice;
import com.pos.hyper.model.invoice.InvoiceMapper;
import com.pos.hyper.repository.InvoiceItemRepository;
import com.pos.hyper.repository.InvoiceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class SaleInvoiceService {

    private final InvoiceService invoiceService;
    private final InvoiceItemService invoiceItemService;
    private final InvoiceMapper invoiceMapper;
    private final InvoiceItemMapper invoiceItemMapper;
    private final InvoiceItemRepository invoiceItemRepository;
    private final InvoiceRepository invoiceRepository;
    private final CustomExceptionHandler customExceptionHandler;


    public SaleInvoiceService(InvoiceService invoiceService, InvoiceItemService invoiceItemService, InvoiceMapper invoiceMapper, InvoiceItemMapper invoiceItemMapper, InvoiceItemRepository invoiceItemRepository, InvoiceRepository invoiceRepository, CustomExceptionHandler customExceptionHandler) {
        this.invoiceService = invoiceService;
        this.invoiceItemService = invoiceItemService;
        this.invoiceMapper = invoiceMapper;
        this.invoiceItemMapper = invoiceItemMapper;
        this.invoiceItemRepository = invoiceItemRepository;
        this.invoiceRepository = invoiceRepository;
        this.customExceptionHandler = customExceptionHandler;
    }

    public ResponseEntity<?> getSale() {
        return ResponseEntity.ok(invoiceService.getAllInvoices()
                .stream()
                .map(
                        invoiceDto -> new SaleInvoiceDto(
                                invoiceDto,
                                invoiceItemService.getAllByInvoiceId(invoiceDto.id())
                        )
                )
                .toList());

    }
    public ResponseEntity<?> getSaleById( Integer id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow();
        List<InvoiceItem> invoiceItems = invoiceItemRepository.findAllByInvoiceId(id);
        return ResponseEntity.ok(new SaleInvoiceDto(invoiceMapper.toInvoiceDto(invoice), invoiceItemMapper.toDtoList(invoiceItems)));
    }
    public ResponseEntity<?> createSale(SaleInvoiceDto saleInvoiceDto) {
        if(!Objects.equals(saleInvoiceDto.items().getFirst().invoiceId(), saleInvoiceDto.invoice().id())){
            throw  customExceptionHandler.handleBadRequestException(" Invoice id does not match");
        }
        return invoiceItemService.createInvoiceItems(saleInvoiceDto);
    }
    @Transactional
    public ResponseEntity<?> returnSale(Integer id, SaleInvoiceDto saleInvoiceDto) {
         if(!Objects.equals(saleInvoiceDto.items().getFirst().invoiceId(), saleInvoiceDto.invoice().id())){
            throw  customExceptionHandler.handleBadRequestException(" Invoice id does not match");
         }
        return ResponseEntity.ok(invoiceItemService.returnInvoiceItems(id, saleInvoiceDto.items()));
    }



}
