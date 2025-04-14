package com.pos.hyper.model;

import java.util.List;

public record SaleInvoiceDto(
        List<SaleInOrderDto> saleInvoiceDtoList
) {
}
