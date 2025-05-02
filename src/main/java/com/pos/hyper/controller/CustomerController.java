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
    List<CustomerDto> findAll(){
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    CustomerDto findById(@PathVariable Integer id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping("")
    CustomerDto save(@Valid @RequestBody CustomerDto customerDto) {

        return customerService.createCustomer(customerDto);
    }

    @PutMapping("/{id}")
    CustomerDto update(@Valid @RequestBody CustomerDto customerDto, @PathVariable Integer id) {
        return customerService.updateCustomer(id, customerDto);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id) {
        customerService.deleteCustomer(id);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exp) {

        return customExceptionHandler.handleMethodArgumentNotValid(exp);
    }
}
