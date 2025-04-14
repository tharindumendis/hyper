package com.pos.hyper.controller;


import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.model.inOrder.InOrder;
import com.pos.hyper.model.inOrder.InOrderDto;
import com.pos.hyper.model.inOrder.InOrderService;
import com.pos.hyper.model.invoice.Invoice;
import com.pos.hyper.model.invoice.InvoiceDto;
import com.pos.hyper.model.product.Product;
import com.pos.hyper.repository.InOrderRepository;
import com.pos.hyper.repository.InvoiceRepository;
import com.pos.hyper.validation.InOrderValidation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class InOrderController {
    private final InOrderService inOrderService;
    private final CustomExceptionHandler customExceptionHandler;


    public InOrderController(InOrderService inOrderService, CustomExceptionHandler customExceptionHandler) {
        this.inOrderService = inOrderService;
        this.customExceptionHandler = customExceptionHandler;
    }

    @GetMapping("")
    public List<InOrderDto> getAllInOrders() {
        return inOrderService.getAllInOrders();
    }
    @GetMapping("/{id}")
    public InOrderDto getInOrderById(@PathVariable Integer id) {
        return inOrderService.getInOrderById(id);
    }
    @PostMapping("")
    public InOrderDto createInOrder(@Valid @RequestBody InOrderDto inOrderDto) {
        return inOrderService.createInOrder(inOrderDto);
    }
    @PutMapping("/{id}")
    public InOrderDto updateInOrder(@PathVariable Integer id, @Valid @RequestBody InOrderDto inOrderDto) {
        return inOrderService.updateInOrder(id, inOrderDto);
    }
    @DeleteMapping("/{id}")
    public void deleteInOrder(@PathVariable Integer id) {
        inOrderService.deleteInOrder(id);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exp) {

        return customExceptionHandler.handleMethodArgumentNotValid(exp);
    }

}
