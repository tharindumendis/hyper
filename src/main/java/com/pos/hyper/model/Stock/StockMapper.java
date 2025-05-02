package com.pos.hyper.model.Stock;

import com.pos.hyper.DTO.StockDto;
import com.pos.hyper.model.grn_item.GRNItem;
import com.pos.hyper.model.product.Product;
import org.springframework.stereotype.Service;

@Service
public class StockMapper {



    public StockDto toDto(Stock stock) {
        return new StockDto(
                stock.getId(),
                stock.getProduct().getId(),
                stock.getGrnItem().getId(),
                stock.getQuantity(),
                stock.getUnitCost()
        );
    }
    public Stock toStock(StockDto stockDto) {
        Product product = new Product();
        product.setId(stockDto.productId());
        GRNItem grnItem = new GRNItem();
        grnItem.setId(stockDto.grnId());

        return Stock.builder()
                .id(stockDto.id())
                .product(product)
                .grnItem(grnItem)
                .quantity(stockDto.quantity())
                .unitCost(stockDto.unitCost())
                .build();
    }
}
