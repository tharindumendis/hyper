package com.pos.hyper.model;

import com.pos.hyper.model.grn.GrnDto;
import com.pos.hyper.model.inventory.InventoryDto;

import java.util.List;

public record StockInventoryDto(
        InventoryDto inventoryDto,
        List<GrnDto> grnDtoList
) {
}
