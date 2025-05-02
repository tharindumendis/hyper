package com.pos.hyper.controller;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.DTO.PurchaseDto;
import com.pos.hyper.model.PurchaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final CustomExceptionHandler customExceptionHandler;

    public PurchaseController(PurchaseService purchaseService, CustomExceptionHandler customExceptionHandler) {
        this.purchaseService = purchaseService;
        this.customExceptionHandler = customExceptionHandler;
    }

    @GetMapping("")
    public List<PurchaseDto> getStockInventory() {

        return purchaseService.getStockGRN();
    }

    @PostMapping("")
    public PurchaseDto createStock(@RequestBody PurchaseDto sIDto) {

        return purchaseService.createStock(sIDto);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exp) {
        return customExceptionHandler.handleMethodArgumentNotValid(exp);
    }


}
