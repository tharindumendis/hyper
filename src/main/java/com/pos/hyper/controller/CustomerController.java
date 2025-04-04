package com.pos.hyper.controller;


import com.pos.hyper.model.Customer;
import com.pos.hyper.repository.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping("")
    List<Customer> findAll(){
        return customerRepository.findAll();
    }

    @PostMapping("")
    Customer save(@Valid @RequestBody Customer customer){

        return customerRepository.save(customer);
    }


}
