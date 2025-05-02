package com.pos.hyper.model.invoice_item;

import com.pos.hyper.DTO.InvoiceItemDto;
import com.pos.hyper.model.invoice.Invoice;
import com.pos.hyper.model.product.Product;
import com.pos.hyper.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceItemMapper {
    private final InvoiceRepository invoiceRepository;
;

    public InvoiceItemMapper(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }


    public InvoiceItemDto toInvoiceItemDto(InvoiceItem invoiceItem) {
        return new InvoiceItemDto(
                invoiceItem.getId(),
                invoiceItem.getInvoice().getId(),
                invoiceItem.getProduct().getId(),
                invoiceItem.getQuantity(),
                invoiceItem.getUnitPrice(),
                invoiceItem.getDiscount(),
                invoiceItem.getCostPrice(),
                invoiceItem.getAmount()
        );

    }
    public InvoiceItem toInvoiceItem(InvoiceItemDto invoiceItemDto) {
        Product product = new Product();
        InvoiceItem invoiceItem = new InvoiceItem();
        Invoice invoice = new Invoice();

        product.setId(invoiceItemDto.productId());
        invoice = invoiceRepository.findById(invoiceItemDto.invoiceId()).orElse(invoice);
        invoiceItem.setInvoice(invoice);
        invoiceItem.setProduct(product);
        invoiceItem.setQuantity(invoiceItemDto.quantity());
        invoiceItem.setUnitPrice(invoiceItemDto.unitPrice());
        invoiceItem.setDiscount(invoiceItemDto.discount());
        invoiceItem.setCostPrice(invoiceItemDto.costPrice());
        invoiceItem.setAmount(invoiceItemDto.amount());
        return invoiceItem;
    }
    public InvoiceItem toInvoiceItem(InvoiceItemDto invoiceItemDto, Product product, Invoice invoice) {
        product.setId(invoiceItemDto.productId());
        InvoiceItem invoiceItem = new InvoiceItem();

        invoiceItem.setInvoice(invoice);
        invoiceItem.setProduct(product);
        invoiceItem.setQuantity(invoiceItemDto.quantity());
        invoiceItem.setUnitPrice(invoiceItemDto.unitPrice());
        invoiceItem.setDiscount(invoiceItemDto.discount());
        invoiceItem.setCostPrice(invoiceItemDto.costPrice());
        invoiceItem.setAmount(invoiceItemDto.amount());
        return invoiceItem;
    }

    public List<InvoiceItemDto> toDtoList(List<InvoiceItem> invoiceItems) {
        return invoiceItems.stream()
                .map(this::toInvoiceItemDto)
                .toList();
    }

    public List<InvoiceItemDto> toInvoiceItemDtoList(List<InvoiceItem> invoiceItems) {
        return invoiceItems.stream()
                .map(this::toInvoiceItemDto)
                .toList();
    }
}
