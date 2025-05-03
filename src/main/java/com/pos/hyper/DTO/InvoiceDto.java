package com.pos.hyper.DTO;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record InvoiceDto(
        Integer id,
        @NotNull(message = "customerId cannot be null")
        Integer customerId,
        Double total,
        String paymentMethod,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
}
