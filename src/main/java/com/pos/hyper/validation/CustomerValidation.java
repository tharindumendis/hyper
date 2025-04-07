package com.pos.hyper.validation;


import com.pos.hyper.model.Customer;
import com.pos.hyper.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CustomerValidation {
    Customer customer;

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> customerValidate(List<Customer> customers){
        List<Customer> newCustomerSet = new ArrayList<>();
        for (Customer customer : customers){
            newCustomerSet.add(customerValidate(customer));
        }
        return newCustomerSet;
    }

    public Customer customerValidate(Customer customer){
        this.customer = customer;

        if(!nameValidation(customer)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Name is required");
        }

        if(!phoneValidation(customer)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Phone is required or already exists");
        }

        if(!emailValidation(customer)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email already exists");
        }
        return customer;
    }

    public boolean nameValidation(Customer customer){
        return customer.getName() != null && !customer.getName().isEmpty();
    }

    public boolean phoneValidation(Customer customer){
        if (customer.getPhone() != null || !customer.getPhone().isEmpty()){
            return false;
        }
        List<Customer> customers = customerRepository.findAll();
        for (Customer c : customers){
            if(!Objects.equals(c.getId(), customer.getId()) &&
            Objects.equals(c.getPhone(), customer.getPhone())){
                return false;
            }
        }
        return true;
    }

    public boolean emailValidation(Customer customer){
        if(customer.getEmail() == null && customer.getEmail().isEmpty()){
            return false;
        }
        List<Customer> customers = customerRepository.findAll();
        for (Customer c : customers){
            if(!Objects.equals (c.getId(), customer.getId()) && Objects.equals(c.getEmail(), customer.getEmail())){
                return false;
            }
        }
        return true;
    }
}
