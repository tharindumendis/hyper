package com.pos.hyper.controller;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.DTO.StockGRNDto;
import com.pos.hyper.model.StockGRNService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockInventoryController {

    private final StockGRNService stockGRNService;
    private final CustomExceptionHandler customExceptionHandler;

    public StockInventoryController(StockGRNService stockGRNService, CustomExceptionHandler customExceptionHandler) {
        this.stockGRNService = stockGRNService;
        this.customExceptionHandler = customExceptionHandler;
    }

    @GetMapping("")
    public List<StockGRNDto> getStockInventory() {

        return stockGRNService.getStockGRN();
    }

    @PostMapping("")
    public StockGRNDto createStock(@RequestBody StockGRNDto sIDto) {

        return stockGRNService.createStock(sIDto);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exp) {
        return customExceptionHandler.handleMethodArgumentNotValid(exp);
    }


}
