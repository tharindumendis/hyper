package com.pos.hyper.controller;

import com.pos.hyper.model.StockInventoryDto;
import com.pos.hyper.model.grn.GrnDto;
import com.pos.hyper.model.grn.GrnService;
import com.pos.hyper.model.inventory.InventoryService;
import com.pos.hyper.repository.GrnRepository;
import com.pos.hyper.repository.InventoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final GrnService grnService;
    private final InventoryService inventoryService;

    public StockController(GrnService grnService, InventoryService inventoryService) {
        this.grnService = grnService;
        this.inventoryService = inventoryService;
    }

    @GetMapping("")
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

    @PostMapping("")
    public StockInventoryDto createStock(@RequestBody StockInventoryDto sIDto) {
        List<GrnDto> grnDtoList = sIDto.grnDtoList();
        return grnService.createStockInventory(grnDtoList);
    }





}
