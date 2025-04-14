package com.pos.hyper.model.inventory;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class InventoryService {
    private final InventoryMapper inventoryMapper;
    private final InventoryRepository inventoryRepository;
    private final CustomExceptionHandler customExceptionHandler;

    public InventoryService(InventoryMapper inventoryMapper, InventoryRepository inventoryRepository, CustomExceptionHandler customExceptionHandler) {
        this.inventoryMapper = inventoryMapper;
        this.inventoryRepository = inventoryRepository;
        this.customExceptionHandler = customExceptionHandler;
    }


    public InventoryDto createInventory(InventoryDto inventoryDto) {
        Inventory inventory = inventoryMapper.toInventory(inventoryDto);
        inventory.setId(null);
        inventory.setTotal(0.0);
        inventory = inventoryRepository.save(inventory);
        return inventoryMapper.toInventoryDto(inventory);
    }
    @Transactional
    public InventoryDto updateInventory(Integer id, InventoryDto inventoryDto) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> customExceptionHandler.handleNotFoundException("Inventory with id " + id + " not found"));
        double total = inventory.getTotal();
        inventory = inventoryMapper.toInventory(inventoryDto, inventory);
        inventory.setTotal(total);
        inventory = inventoryRepository.save(inventory);
        return inventoryMapper.toInventoryDto(inventory);
    }
    public List<InventoryDto> getAllInventories() {
        List<Inventory> inventories = inventoryRepository.findAll();
        return inventories.stream().map(inventoryMapper::toInventoryDto).toList();
    }
    public InventoryDto getInventoryById(Integer id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> customExceptionHandler.handleNotFoundException("Inventory with id " + id + " not found"));
        return inventoryMapper.toInventoryDto(inventory);
    }
    public void deleteInventory(Integer id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> customExceptionHandler.handleNotFoundException("Inventory with id " + id + " not found"));
        inventoryRepository.delete(inventory);
    }


}
