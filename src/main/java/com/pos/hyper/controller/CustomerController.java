package com.pos.hyper.controller;


import com.pos.hyper.model.customer.Customer;
import com.pos.hyper.model.customer.CustomerDto;
import com.pos.hyper.repository.CustomerRepository;
import com.pos.hyper.validation.CustomerValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private final CustomerRepository customerRepository;

    @Autowired
    private CustomerValidation customerValidation;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping("")
    List<Customer> findAll(){
        return customerRepository.findAll();
    }

    @PostMapping("")
    Customer save(@Valid @RequestBody CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setName(customerDto.name());
        customer.setAddress(customerDto.address());
        customer.setPhone(customerDto.phone());
        customer.setEmail(customerDto.email());
        customer= customerValidation.customerValidate(customer);
        return customerRepository.save(customer);
    }

    @PutMapping("/{id}")
    Customer update(@Valid @RequestBody Customer customer, @PathVariable Integer id) {
        customer = customerValidation.customerValidate(customer);
        Customer cust = customerRepository.findById(id).get();
        cust.setName(customer.getName());
        cust.setAddress(customer.getAddress());
        cust.setPhone(customer.getPhone());
        cust.setEmail(customer.getEmail());
        return customerRepository.save(cust);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id) {
        customerRepository.deleteById(id);
    }


}
