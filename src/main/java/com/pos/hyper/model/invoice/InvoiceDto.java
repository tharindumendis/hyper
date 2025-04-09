package com.pos.hyper.model.invoice;

public record InvoiceDto(
        Integer id,
        Integer customerId,
        Double total
) {
}
