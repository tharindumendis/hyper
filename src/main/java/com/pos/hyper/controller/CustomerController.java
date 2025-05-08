package com.pos.hyper.controller;


import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.DTO.CustomerDto;
import com.pos.hyper.model.customer.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomExceptionHandler customExceptionHandler;

    public CustomerController(CustomerService customerService, CustomExceptionHandler customExceptionHandler) {
        this.customerService = customerService;
        this.customExceptionHandler = customExceptionHandler;

    }

    @GetMapping("")
    public ResponseEntity<?> findAll(){
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping("")
    public ResponseEntity<?> save(@Valid @RequestBody CustomerDto customerDto) {
        return customerService.createCustomer(customerDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody CustomerDto customerDto, @PathVariable Integer id) {
        return customerService.updateCustomer(id, customerDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        customerService.deleteCustomer(id);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exp) {

        return customExceptionHandler.handleMethodArgumentNotValid(exp);
    }
}
