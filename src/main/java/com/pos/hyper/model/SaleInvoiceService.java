package com.pos.hyper.model;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.model.inOrder.InOrder;
import com.pos.hyper.model.inOrder.InOrderMapper;
import com.pos.hyper.model.inOrder.InOrderService;
import com.pos.hyper.model.invoice.Invoice;
import com.pos.hyper.model.invoice.InvoiceMapper;
import com.pos.hyper.model.invoice.InvoiceService;
import com.pos.hyper.repository.InOrderRepository;
import com.pos.hyper.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class SaleInvoiceService {

    private final InvoiceService invoiceService;
    private final InOrderService inOrderService;
    private final InvoiceMapper invoiceMapper;
    private final InOrderMapper inOrderMapper;
    private final InOrderRepository inOrderRepository;
    private final InvoiceRepository invoiceRepository;
    private final CustomExceptionHandler customExceptionHandler;


    public SaleInvoiceService(InvoiceService invoiceService, InOrderService inOrderService, InvoiceMapper invoiceMapper, InOrderMapper inOrderMapper, InOrderRepository inOrderRepository, InvoiceRepository invoiceRepository, CustomExceptionHandler customExceptionHandler) {
        this.invoiceService = invoiceService;
        this.inOrderService = inOrderService;
        this.invoiceMapper = invoiceMapper;
        this.inOrderMapper = inOrderMapper;
        this.inOrderRepository = inOrderRepository;
        this.invoiceRepository = invoiceRepository;
        this.customExceptionHandler = customExceptionHandler;
    }

    public List<SaleInvoiceDto> getSale() {
        return invoiceService.getAllInvoices()
                .stream()
                .map(
                        invoiceDto -> new SaleInvoiceDto(
                                invoiceDto,
                                inOrderService.getAllByInvoiceId(invoiceDto.id())
                        )
                )
                .toList();

    }
    public SaleInvoiceDto getSaleById( Integer id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow();
        List<InOrder> inOrders = inOrderRepository.findAllByInvoiceId(id);
        return new SaleInvoiceDto(invoiceMapper.toInvoiceDto(invoice), inOrderMapper.toDtoList(inOrders));
    }
    public SaleInvoiceDto createSale(SaleInvoiceDto saleInvoiceDto) {
        if(!Objects.equals(saleInvoiceDto.items().getFirst().invoiceId(), saleInvoiceDto.invoice().id())){
            throw  customExceptionHandler.handleBadRequestException(" Invoice id does not match");
        }
        return inOrderService.createInOrders(saleInvoiceDto.items());
    }
    @Transactional
    public SaleInvoiceDto updateSale(Integer id, SaleInvoiceDto saleInvoiceDto) {
         if(!Objects.equals(saleInvoiceDto.items().getFirst().invoiceId(), saleInvoiceDto.invoice().id())){
            throw  customExceptionHandler.handleBadRequestException(" Invoice id does not match");
         }
        return inOrderService.updateInOrders(id, saleInvoiceDto.items());
    }



}
