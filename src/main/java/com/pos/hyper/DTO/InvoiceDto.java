package com.pos.hyper.DTO;

import com.pos.hyper.model.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record InvoiceDto(
        Integer id,
        @NotNull(message = "customerId cannot be null")
        Integer customerId,
        Double total,
        String paymentMethod
) {
}
