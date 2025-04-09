package com.pos.hyper.model.product;

import com.pos.hyper.model.Unitt;
import jakarta.annotation.Nullable;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductDto(
        Integer id,
        @NotBlank(message = "barcode cannot be blank or empty")
        String barcode,
        @NotBlank(message = "name cannot be blank or empty")
        String name,
        @NotNull(message = "categoryId cannot be null")
        Integer categoryId,
        @NotNull(message = "unit cannot be null")
        Unitt unit,
        String description,
        String image,
        Double price,
        Double cost ,
        boolean isActive,
        @PositiveOrZero
        @Nullable
        Double quantity

) {
}
