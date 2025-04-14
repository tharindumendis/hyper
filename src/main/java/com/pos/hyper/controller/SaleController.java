package com.pos.hyper.controller;

import com.pos.hyper.model.SaleInOrderDto;
import com.pos.hyper.model.inOrder.InOrder;
import com.pos.hyper.model.inOrder.InOrderDto;
import com.pos.hyper.model.inOrder.InOrderMapper;
import com.pos.hyper.model.inOrder.InOrderService;
import com.pos.hyper.model.invoice.Invoice;
import com.pos.hyper.model.invoice.InvoiceMapper;
import com.pos.hyper.model.invoice.InvoiceService;
import com.pos.hyper.repository.InOrderRepository;
import com.pos.hyper.repository.InvoiceRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/sale")
public class SaleController {
    private final InvoiceService invoiceService;
    private final InOrderService inOrderService;
    private final InvoiceMapper invoiceMapper;
    private final InOrderMapper inOrderMapper;
    private final InOrderRepository inOrderRepository;
    private final InvoiceRepository invoiceRepository;

    public SaleController(InvoiceService invoiceService, InOrderService inOrderService, InvoiceMapper invoiceMapper, InOrderMapper inOrderMapper, InOrderRepository inOrderRepository, InvoiceRepository invoiceRepository) {
        this.invoiceService = invoiceService;
        this.inOrderService = inOrderService;
        this.invoiceMapper = invoiceMapper;
        this.inOrderMapper = inOrderMapper;
        this.inOrderRepository = inOrderRepository;
        this.invoiceRepository = invoiceRepository;
    }
    @GetMapping("")
    public List<SaleInOrderDto> getSale() {
        List<SaleInOrderDto> saleInvoiceDtoList = new ArrayList<>();
         saleInvoiceDtoList = invoiceService.getAllInvoices()
                 .stream()
                 .map(
                         invoiceDto -> new SaleInOrderDto(
                                 invoiceDto,
                                 inOrderService.getAllByInvoiceId(invoiceDto.id())
                         )
                 )
                 .toList();
        return saleInvoiceDtoList;

    }
    @GetMapping("/{id}")
    public SaleInOrderDto getSaleById(@PathVariable Integer id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow();
        List<InOrder> inOrders = inOrderRepository.findAllByInvoiceId(id);
        return new SaleInOrderDto(invoiceMapper.toInvoiceDto(invoice), inOrderMapper.toDtoList(inOrders));
    }






    @PostMapping("")
    public List<InOrderDto> createSale(@RequestBody SaleInOrderDto saleInvoiceDto) {
        List<InOrderDto> inOrderDtoList = saleInvoiceDto.inOrdersDto();

        return inOrderService.createInOrders(inOrderDtoList);
    }






}
