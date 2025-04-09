package com.pos.hyper.controller;

import com.pos.hyper.model.inventory.Inventory;
import com.pos.hyper.repository.InventoryRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryRepository inventoryRepository;


    public InventoryController(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @GetMapping("")
    List<Inventory> getInventory(){
        return inventoryRepository.findAll();
    }

    @GetMapping("/{id}")
    Inventory getInventoryById(@PathVariable Integer id){
        return inventoryRepository.findById(id).get();
    }

    @PostMapping("")
    Inventory addInventory(@Valid @RequestBody Inventory inventory){
        return inventoryRepository.save(inventory);
    }

    @PutMapping("/{id}")
    Inventory updateInventory(@PathVariable Integer id, @Valid @RequestBody Inventory inventory) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

    }
    @DeleteMapping("/{id}")
    void deleteInventory(@PathVariable Integer id){
        inventoryRepository.deleteById(id);
    }



}
