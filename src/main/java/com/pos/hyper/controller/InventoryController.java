package com.pos.hyper.controller;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.model.inventory.InventoryDto;
import com.pos.hyper.model.inventory.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    private final CustomExceptionHandler customExceptionHandler;
    public InventoryController(InventoryService inventoryService, CustomExceptionHandler customExceptionHandler) {
        this.inventoryService = inventoryService;
        this.customExceptionHandler = customExceptionHandler;
    }

    @GetMapping("")
    public List<InventoryDto> getAllInventories() {
        return inventoryService.getAllInventories();
    }
    @GetMapping("/{id}")
    public InventoryDto getInventoryById(@PathVariable Integer id) {
        return inventoryService.getInventoryById(id);
    }
    @PostMapping(path = "",consumes = MediaType.APPLICATION_JSON_VALUE)
    public InventoryDto createInventory(@Valid @RequestBody InventoryDto inventoryDto) {
        return inventoryService.createInventory(inventoryDto);
    }
    @PutMapping("/{id}")
    public InventoryDto updateInventory(@PathVariable Integer id, @Valid @RequestBody InventoryDto inventoryDto) {
        return inventoryService.updateInventory(id, inventoryDto);
    }
    @DeleteMapping("/{id}")
    public void deleteInventory(@PathVariable Integer id) {
        inventoryService.deleteInventory(id);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exp) {
        return customExceptionHandler.handleMethodArgumentNotValid(exp);
    }

}
