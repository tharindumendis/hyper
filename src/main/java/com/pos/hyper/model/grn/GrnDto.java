package com.pos.hyper.model.grn;

import jakarta.validation.constraints.*;

public record GrnDto(
        Integer id,
        @NotNull(message = "inventoryId cannot be null")
        Integer inventoryId,
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
