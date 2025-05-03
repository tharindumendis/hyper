package com.pos.hyper.model.Stock;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
public class StockQty {
    private Integer stockId;
    private Double quantity;

    public StockQty(Integer stockId, Double quantity) {
        this.stockId = stockId;
        this.quantity = quantity;
    }
}
