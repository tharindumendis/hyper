package com.pos.hyper.DTO;

import com.pos.hyper.model.Unitt;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
        Integer discount,
        Double price,
        boolean isActive

) {
}
