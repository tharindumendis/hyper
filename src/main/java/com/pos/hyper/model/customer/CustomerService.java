package com.pos.hyper.model.customer;

import com.pos.hyper.DTO.CustomerDto;
import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.repository.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final CustomExceptionHandler customExceptionHandler;


    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper, CustomExceptionHandler customExceptionHandler) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.customExceptionHandler = customExceptionHandler;
    }

    public ResponseEntity<?> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return ResponseEntity.ok(customers.stream().map(customerMapper::toCustomerDto).toList());
    }
    public ResponseEntity<?> getCustomerById(Integer id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer == null) {
            return customExceptionHandler.notFoundException("Customer with id " + id + " not found");
        }
        return ResponseEntity.ok(customerMapper.toCustomerDto(customer));
    }

    public ResponseEntity<?> createCustomer(CustomerDto customerDto) {
        ResponseEntity<?> error = validateCustomer(customerDto);
        if( error != null) {
            return error;
        }
        Customer customer = customerMapper.toCustomer(customerDto);
        customer.setId(null);
        if(customer.getIsActive()==null) {
            customer.setIsActive(Boolean.TRUE);
        }
        return ResponseEntity.ok(customerMapper.toCustomerDto(customerRepository.save(customer)));
    }
    @Transactional
    public ResponseEntity<?> updateCustomer(Integer id, CustomerDto customerDto) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer == null) {
            customExceptionHandler.notFoundException("Customer with id " + id + " not found");
        }
        customer = customerMapper.toCustomer(customerDto, customer);
        customer.setId(id);
        customer = customerRepository.save(customer);
        return ResponseEntity.ok(customerMapper.toCustomerDto(customer));
    }

    public void deleteCustomer(Integer id) {
        customerRepository.deleteById(id);
    }

    private ResponseEntity<?> validateCustomer(CustomerDto customerDto) {
        List<String> errors = new ArrayList<>();
        if(customerRepository.existsByEmail(customerDto.email())) {
            errors.add("Email already exists");
        }
        if(customerRepository.existsByPhone(customerDto.phone())) {
            errors.add("Phone already exists");
        }
        if(!errors.isEmpty()) {
            return customExceptionHandler.badRequestExceptionSet(errors);
        }
        return null;
    }

}
