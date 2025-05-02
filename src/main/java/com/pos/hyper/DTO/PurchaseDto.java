package com.pos.hyper.DTO;

import java.util.List;

public record PurchaseDto(
        GRNDto grn,
        List<GRNItemDto> items
) {
}
