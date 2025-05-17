package com.pos.hyper.controller;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.DTO.PurchaseDto;
import com.pos.hyper.service.PurchaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final CustomExceptionHandler customExceptionHandler;

    public PurchaseController(PurchaseService purchaseService, CustomExceptionHandler customExceptionHandler) {
        this.purchaseService = purchaseService;
        this.customExceptionHandler = customExceptionHandler;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ResponseEntity<?> getStockInventory() {

        return purchaseService.getStockGRN();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<?> createStock(@RequestBody PurchaseDto sIDto) {

        return purchaseService.createPurchase(sIDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity <?> getPurchaseById(@PathVariable Integer id) {
        return purchaseService.getPurchaseById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> returnPurchase(@PathVariable Integer id, @RequestBody PurchaseDto sIDto) {
        return purchaseService.returnPurchase(id, sIDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exp) {
        return customExceptionHandler.handleMethodArgumentNotValid(exp);
    }


}
