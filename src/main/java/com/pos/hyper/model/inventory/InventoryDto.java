package com.pos.hyper.model.inventory;

import com.pos.hyper.model.grn.Grn;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public record InventoryDto(
        Integer id,
        @NotNull(message = "supplierId cannot be null")
        Integer supplierId,
        @PositiveOrZero(message = "total cannot be negative")
        Double total
) {
}
