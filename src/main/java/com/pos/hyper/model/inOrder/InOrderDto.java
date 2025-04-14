package com.pos.hyper.model.inOrder;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record InOrderDto(
        Integer id,
        @NotNull(message = "invoiceId cannot be null")
        Integer invoiceId,
        @NotNull(message = "productId cannot be null")
        Integer productId,
        @NotNull(message = "quantity cannot be null")
        @Positive
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
