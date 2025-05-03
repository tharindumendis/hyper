package com.pos.hyper.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

public record GRNDto(
        Integer id,
        @NotNull(message = "supplierId cannot be null")
        Integer supplierId,
        @PositiveOrZero(message = "total cannot be negative")
        Double total,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
