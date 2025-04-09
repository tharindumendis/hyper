package com.pos.hyper.model.inOrder;

import jakarta.validation.constraints.NotNull;

public record InOrderDto(
        Integer id,
        @NotNull
        Integer invoiceId,
        @NotNull
        Integer productId,
        @NotNull
        Double quantity,
        @NotNull
        Double unitPrice,
        Integer discount,
        Double costPrice,
        Double amount
) {
}
