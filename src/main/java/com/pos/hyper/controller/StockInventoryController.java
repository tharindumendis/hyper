package com.pos.hyper.controller;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.model.StockInventoryDto;
import com.pos.hyper.model.StockInventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockInventoryController {

    private final StockInventoryService stockInventoryService;
    private final CustomExceptionHandler customExceptionHandler;

    public StockInventoryController(StockInventoryService stockInventoryService, CustomExceptionHandler customExceptionHandler) {
        this.stockInventoryService = stockInventoryService;
        this.customExceptionHandler = customExceptionHandler;
    }

    @GetMapping("")
    public List<StockInventoryDto> getStockInventory() {

        return stockInventoryService.getStockInventory();
    }

    @PostMapping("")
    public StockInventoryDto createStock(@RequestBody StockInventoryDto sIDto) {

        return stockInventoryService.createStock(sIDto);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exp) {
        return customExceptionHandler.handleMethodArgumentNotValid(exp);
    }


}
