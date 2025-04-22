package com.pos.hyper.controller;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.model.SaleInvoiceDto;
import com.pos.hyper.model.SaleInvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sale")
public class SaleInvoiceController {

    private final SaleInvoiceService saleInvoiceService;
    private final CustomExceptionHandler customExceptionHandler;

    public SaleInvoiceController(SaleInvoiceService saleInvoiceService, CustomExceptionHandler customExceptionHandler) {
        this.saleInvoiceService = saleInvoiceService;
        this.customExceptionHandler = customExceptionHandler;
    }
    @GetMapping("")
    public List<SaleInvoiceDto> getSale() {
        return saleInvoiceService.getSale();

    }
    @GetMapping("/{id}")
    public SaleInvoiceDto getSaleById(@PathVariable Integer id) {
       return saleInvoiceService.getSaleById(id);
    }

    @PostMapping("")
    public SaleInvoiceDto createSale(@RequestBody SaleInvoiceDto saleInvoiceDto) {
        return saleInvoiceService.createSale(saleInvoiceDto);
    }

    @PutMapping("/{id}")
    public SaleInvoiceDto updateSale(@PathVariable Integer id, @RequestBody SaleInvoiceDto saleInvoiceDto) {
        return saleInvoiceService.updateSale(id, saleInvoiceDto);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exp) {
        return customExceptionHandler.handleMethodArgumentNotValid(exp);
    }




}
