package com.pos.hyper.model.customer;

import com.pos.hyper.model.invoice.Invoice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerMapper {
    public CustomerDto toCustomerDto(Customer customer) {
        return new CustomerDto(
                customer.getId(),
                customer.getName(),
                customer.getAddress(),
                customer.getPhone(),
                customer.getEmail()
        );
    }
    public Customer toCustomer(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setId(customerDto.id());
        customer.setName(customerDto.name());
        customer.setAddress(customerDto.address());
        customer.setPhone(customerDto.phone());
        customer.setEmail(customerDto.email());
        return customer;

    }
    public Customer toCustomer(CustomerDto customerDto, Customer customer) {
        customer.setName(customerDto.name());
        customer.setAddress(customerDto.address());
        customer.setPhone(customerDto.phone());
        customer.setEmail(customerDto.email());
        return customer;
    }
    public Customer toCustomer(CustomerDto customerDto, Customer customer, List<Invoice> invoices) {
        customer.setName(customerDto.name());
        customer.setAddress(customerDto.address());
        customer.setPhone(customerDto.phone());
        customer.setEmail(customerDto.email());
        customer.setInvoices(invoices);
        return customer;
    }

}
