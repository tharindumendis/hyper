package com.pos.hyper.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record InvoiceItemDto(
        Integer id,
        @NotNull(message = "invoiceId cannot be null")
        Integer invoiceId,
        @NotNull(message = "productId cannot be null")
        Integer productId,
        @PositiveOrZero(message = "quantity cannot be negative")
        Double quantity,

        @NotNull(message = "unitPrice cannot be null")
        @PositiveOrZero(message = "unitPrice cannot be negative")
        Double unitPrice,
        @PositiveOrZero(message = "discount cannot be negative")
        Integer discount,


        @PositiveOrZero(message = "amount cannot be negative")
        Double costPrice,

        @NotNull
        @PositiveOrZero(message = "amount cannot be negative")
        Double amount
) {
}
