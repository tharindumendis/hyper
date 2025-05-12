package com.pos.hyper.DTO;

import com.pos.hyper.model.Unitt;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductBillDto(
        Integer id,
        String barcode,
        String name,
        Integer categoryId,
        Unitt unit,
        String description,
        String image,
        Integer discount,
        Double price,
        boolean isActive

) {
}
