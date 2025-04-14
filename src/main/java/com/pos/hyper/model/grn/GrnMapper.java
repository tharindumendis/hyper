package com.pos.hyper.model.grn;

import com.pos.hyper.model.inventory.Inventory;
import com.pos.hyper.model.product.Product;
import org.springframework.stereotype.Service;

@Service
public class GrnMapper {
    Inventory inventory = new Inventory();
    Product product = new Product();

    public GrnDto toGrnDto(Grn grn) {
        return new GrnDto(
                grn.getId(),
                grn.getInventory().getId(),
                grn.getProduct().getId(),
                grn.getQuantity(),
                grn.getUnitCost(),
                grn.getDiscount(),
                grn.getAmount()
        );
    }
    public Grn toGrn(GrnDto grnDto,Product product) {
        return getGrn(grnDto, product, inventory);
    }
    public Grn toGrn(GrnDto grnDto,Inventory inventory) {
        return getGrn(grnDto, product, inventory);
    }
    public Grn toGrn(GrnDto grnDto,Product product,Inventory inventory) {
        return getGrn(grnDto, product, inventory);
    }

    private Grn getGrn(GrnDto grnDto, Product product, Inventory inventory) {
        Grn grn = new Grn();
        product.setId(grnDto.productId());
        inventory.setId(grnDto.inventoryId());
        grn.setId(grnDto.id());
        grn.setInventory(inventory);
        grn.setProduct(product);
        grn.setQuantity(grnDto.quantity());
        grn.setUnitCost(grnDto.unitCost());
        grn.setDiscount(grnDto.discount());
        grn.setAmount(grnDto.amount());
        return grn;
    }

}
