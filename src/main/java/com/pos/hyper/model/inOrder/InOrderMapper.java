package com.pos.hyper.model.inOrder;

import com.pos.hyper.model.invoice.Invoice;
import com.pos.hyper.model.product.Product;
import com.pos.hyper.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InOrderMapper {
    private final InvoiceRepository invoiceRepository;
;

    public InOrderMapper(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }


    public InOrderDto toInOrderDto(InOrder inOrder) {
        return new InOrderDto(
                inOrder.getId(),
                inOrder.getInvoice().getId(),
                inOrder.getProduct().getId(),
                inOrder.getQuantity(),
                inOrder.getUnitPrice(),
                inOrder.getDiscount(),
                inOrder.getCostPrice(),
                inOrder.getAmount()
        );

    }
    public InOrder toInOrder(InOrderDto inOrderDto) {
        Product product = new Product();
        InOrder inOrder = new InOrder();
        Invoice invoice = new Invoice();

        product.setId(inOrderDto.productId());
        invoice = invoiceRepository.findById(inOrderDto.invoiceId()).orElse(invoice);
        inOrder.setInvoice(invoice);
        inOrder.setProduct(product);
        inOrder.setQuantity(inOrderDto.quantity());
        inOrder.setUnitPrice(inOrderDto.unitPrice());
        inOrder.setDiscount(inOrderDto.discount());
        inOrder.setCostPrice(inOrderDto.costPrice());
        inOrder.setAmount(inOrderDto.amount());
        return inOrder;
    }
    public InOrder toInOrder(InOrderDto inOrderDto,Product product, Invoice invoice) {
        product.setId(inOrderDto.productId());
        InOrder inOrder = new InOrder();

        inOrder.setInvoice(invoice);
        inOrder.setProduct(product);
        inOrder.setQuantity(inOrderDto.quantity());
        inOrder.setUnitPrice(inOrderDto.unitPrice());
        inOrder.setDiscount(inOrderDto.discount());
        inOrder.setCostPrice(inOrderDto.costPrice());
        inOrder.setAmount(inOrderDto.amount());
        return inOrder;
    }

    public List<InOrderDto> toDtoList(List<InOrder> inOrders) {
        return inOrders.stream()
                .map(this::toInOrderDto)
                .toList();
    }
}
