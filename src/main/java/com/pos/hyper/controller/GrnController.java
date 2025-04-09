package com.pos.hyper.controller;

import com.pos.hyper.model.grn.Grn;
import com.pos.hyper.repository.GrnRepository;
import com.pos.hyper.repository.InventoryRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grn")
public class GrnController {

    private final GrnRepository grnRepository;
    private final InventoryRepository inventoryRepository;


    public GrnController(GrnRepository grnRepository, InventoryRepository inventoryRepository) {
        this.grnRepository = grnRepository;
        this.inventoryRepository = inventoryRepository;
    }


    @GetMapping("")
    List<Grn> getGrn(){
        return grnRepository.findAll();
    }

    @GetMapping("/{id}")
    Grn getGrnById(Integer id){
        return grnRepository.findById(id).get();
    }

    @PostMapping("")
    Grn addGrn(@Valid @RequestBody Grn grn){
        return grnRepository.save(grn);
    }

    @PutMapping("/{id}")
    Grn updateGrn(@PathVariable Integer id, @Valid @RequestBody Grn grn) {
        return grnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

    }
    @DeleteMapping("/{id}")
    void deleteGrn(@PathVariable Integer id){
        grnRepository.deleteById(id);
    }

}
