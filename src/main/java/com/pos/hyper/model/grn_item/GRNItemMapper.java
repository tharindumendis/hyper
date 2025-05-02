package com.pos.hyper.model.grn_item;

import com.pos.hyper.DTO.GRNItemDto;
import com.pos.hyper.model.grn.GRN;
import com.pos.hyper.model.product.Product;
import org.springframework.stereotype.Service;

@Service
public class GRNItemMapper {
    GRN grn = new GRN();
    Product product = new Product();

    public GRNItemDto toGRNItemDto(GRNItem grnItem) {
        return new GRNItemDto(
                grnItem.getId(),
                grnItem.getGrn().getId(),
                grnItem.getProduct().getId(),
                grnItem.getQuantity(),
                grnItem.getUnitCost(),
                grnItem.getDiscount(),
                grnItem.getAmount()
        );
    }
    public GRNItem toGRNItem(GRNItemDto grnItemDto, Product product) {
        return getGRNItem(grnItemDto, product, grn);
    }
    public GRNItem toGRNItem(GRNItemDto grnItemDto, GRN grn) {
        return getGRNItem(grnItemDto, product, grn);
    }
    public GRNItem toGRNItem(GRNItemDto grnItemDto, Product product, GRN grn) {
        return getGRNItem(grnItemDto, product, grn);
    }

    private GRNItem getGRNItem(GRNItemDto grnItemDto, Product product, GRN grn) {
        GRNItem grnItem = new GRNItem();
        product.setId(grnItemDto.productId());
        grn.setId(grnItemDto.GRNId());
        grnItem.setId(grnItemDto.id());
        grnItem.setGrn(grn);
        grnItem.setProduct(product);
        grnItem.setQuantity(grnItemDto.quantity());
        grnItem.setUnitCost(grnItemDto.unitCost());
        grnItem.setDiscount(grnItemDto.discount());
        grnItem.setAmount(grnItemDto.amount());
        return grnItem;
    }

}
