package com.pos.hyper.model.Stock;

import com.pos.hyper.model.grn.Grn;
import com.pos.hyper.model.product.Product;
import org.springframework.stereotype.Service;

@Service
public class StockMapper {



    public StockDto toDto(Stock stock) {
        return new StockDto(
                stock.getId(),
                stock.getProduct().getId(),
                stock.getGrn().getId(),
                stock.getQuantity(),
                stock.getUnitCost()
        );
    }
    public Stock toStock(StockDto stockDto) {
        Product product = new Product();
        product.setId(stockDto.productId());
        Grn grn = new Grn();
        grn.setId(stockDto.grnId());

        return Stock.builder()
                .id(stockDto.id())
                .product(product)
                .grn(grn)
                .quantity(stockDto.quantity())
                .unitCost(stockDto.unitCost())
                .build();
    }
}
