package com.pos.hyper.model.product;

import com.pos.hyper.model.Unitt;

public record ProductStockDto(
        Integer id,
        String barcode,
        String name,
        Integer categoryId,
        Double price,
        String description,
        Integer discount,
        String image,
        Boolean isActive,
        String unit,
        Double stock,
        Double cost
) {
}
