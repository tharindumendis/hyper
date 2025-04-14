package com.pos.hyper.model.invoice;

import com.pos.hyper.model.inOrder.InOrder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public record InvoiceDto(
        Integer id,
        @NotNull(message = "customerId cannot be null")
        Integer customerId,
        Double total
) {
}
