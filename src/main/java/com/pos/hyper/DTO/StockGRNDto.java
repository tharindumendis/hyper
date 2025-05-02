package com.pos.hyper.DTO;

import java.util.List;

public record StockGRNDto(
        GRNDto grn,
        List<GRNItemDto> items
) {
}
