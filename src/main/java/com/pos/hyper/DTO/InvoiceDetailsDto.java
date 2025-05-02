package com.pos.hyper.DTO;

import jakarta.validation.constraints.NotNull;

public record InvoiceDetailsDto(
        Integer id,
        @NotNull(message = "customerId cannot be null")
        Integer customerId,
        Double total
) {
}
