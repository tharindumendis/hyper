package com.pos.hyper.DTO;

import jakarta.validation.constraints.*;

public record GRNItemDto(
        Integer id,
        @NotNull(message = "GRNId cannot be null")
        Integer GRNId,
        @NotNull(message = "productId cannot be null")
        Integer productId,
        @PositiveOrZero(message = "quantity cannot be negative")
        Double quantity,
        @PositiveOrZero(message = "unitCost cannot be negative")
        Double unitCost,
        @Min(0)
        @Max(100)
        Integer discount,
        @PositiveOrZero(message = "amount cannot be negative")
        Double amount
) {
}
