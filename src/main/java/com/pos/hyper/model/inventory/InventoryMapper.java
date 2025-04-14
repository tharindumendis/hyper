package com.pos.hyper.model.inventory;

import com.pos.hyper.model.supplier.Supplier;
import org.springframework.stereotype.Service;

@Service
public class InventoryMapper {

    private final Supplier supplier = new Supplier();

    public InventoryDto toInventoryDto(Inventory inventory) {
        return new InventoryDto(
                inventory.getId(),
                inventory.getSupplier().getId(),
                inventory.getTotal()
        );
    }
    public Inventory toInventory(InventoryDto inventoryDto) {
        supplier.setId(inventoryDto.supplierId());
        Inventory inventory = new Inventory();
        inventory.setId(inventoryDto.id());
        inventory.setSupplier(supplier);
        inventory.setTotal(inventoryDto.total());
        return inventory;
    }
    public Inventory toInventory(InventoryDto inventoryDto, Inventory inventory) {
        supplier.setId(inventoryDto.supplierId());
        inventory.setSupplier(supplier);
        inventory.setTotal(inventoryDto.total());
        return inventory;
    }
    public Inventory toInventory(InventoryDto inventoryDto, Inventory inventory, Supplier supplier) {
        inventory.setSupplier(supplier);
        inventory.setTotal(inventoryDto.total());
        return inventory;
    }

}
