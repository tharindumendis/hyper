package com.pos.hyper.model.Stock;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record StockDto(
        Integer id,
        @NotNull(message = "grnId cannot be null")
        Integer grnId,
        @NotNull(message = "productId cannot be null")
        Integer productId,
        @PositiveOrZero(message = "quantity cannot be negative")
        Double quantity,
        @PositiveOrZero(message = "unitCost cannot be negative")
        Double unitCost
) {

}
