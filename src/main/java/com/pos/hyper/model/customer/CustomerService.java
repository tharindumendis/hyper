package com.pos.hyper.model.customer;

import com.pos.hyper.DTO.CustomerDto;
import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.repository.CustomerRepository;
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

    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(customerMapper::toCustomerDto).toList();
    }
    public CustomerDto getCustomerById(Integer id) {
        Customer customer = customerRepository
                .findById(id).orElseThrow(() -> customExceptionHandler
                        .handleNotFoundException("Customer with id " + id + " not found"));
        return customerMapper.toCustomerDto(customer);
    }

    public CustomerDto createCustomer(CustomerDto customerDto) {
        validateCustomer(customerDto);
        Customer customer = customerMapper.toCustomer(customerDto);
        customer.setId(null);
        if(customer.getIsActive()==null){
            customer.setIsActive(Boolean.TRUE);
        }
        customer = customerRepository.save(customer);
        return customerMapper.toCustomerDto(customer);
    }
    @Transactional
    public CustomerDto updateCustomer(Integer id, CustomerDto customerDto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(()-> customExceptionHandler
                        .handleNotFoundException("Customer with id " + id + " not found"));
        customer = customerMapper.toCustomer(customerDto, customer);
        customer.setId(id);
        customer = customerRepository.save(customer);
        return customerMapper.toCustomerDto(customer);
    }
    public void deleteCustomer(Integer id) {
        customerRepository.deleteById(id);
    }

    private void validateCustomer(CustomerDto customerDto) {
        List<String> errors = new ArrayList<>();
        if(customerRepository.existsByEmail(customerDto.email())) {
            errors.add("Email already exists");
        }
        if(customerRepository.existsByPhone(customerDto.phone())) {
            errors.add("Phone already exists");
        }
        if(!errors.isEmpty()) {
            throw customExceptionHandler.handleBadRequestExceptionSet(errors);
        }
    }

}
