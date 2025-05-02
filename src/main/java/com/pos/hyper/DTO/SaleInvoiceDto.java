package com.pos.hyper.DTO;

import java.util.List;

public record SaleInvoiceDto(
        InvoiceDto invoice,
        List<InvoiceItemDto> items
) {
}
