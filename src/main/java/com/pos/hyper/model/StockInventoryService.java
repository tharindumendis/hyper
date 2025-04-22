package com.pos.hyper.model;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.model.grn.GrnDto;
import com.pos.hyper.model.grn.GrnService;
import com.pos.hyper.model.inventory.InventoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class StockInventoryService {
    private final GrnService grnService;
    private final InventoryService inventoryService;
    private final CustomExceptionHandler customExceptionHandler;


    public StockInventoryService(GrnService grnService, InventoryService inventoryService, CustomExceptionHandler customExceptionHandler) {
        this.grnService = grnService;
        this.inventoryService = inventoryService;
        this.customExceptionHandler = customExceptionHandler;
    }



    public List<StockInventoryDto> getStockInventory() {

        return inventoryService
                .getAllInventories()
                .stream()
                .map(
                        inventoryDto -> new StockInventoryDto(
                                inventoryDto,
                                grnService.getGrnByInventoryId(inventoryDto.id())
                        )
                ).toList();
    }
    public StockInventoryDto createStock(StockInventoryDto sIDto) {
        if(!Objects.equals(sIDto.grnDtoList().getFirst().inventoryId(), sIDto.inventoryDto().id())){
            throw customExceptionHandler.handleBadRequestException("Inventory ID does not match");
        }

        return grnService.createStockInventory(sIDto.grnDtoList());
    }



}
