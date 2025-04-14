package com.pos.hyper.model;

import com.pos.hyper.model.inOrder.InOrderDto;
import com.pos.hyper.model.invoice.InvoiceDto;

import java.util.List;

public record SaleInOrderDto(
        InvoiceDto invoiceDto,
        List<InOrderDto> inOrdersDto
) {
}
