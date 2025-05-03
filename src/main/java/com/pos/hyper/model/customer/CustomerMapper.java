package com.pos.hyper.model.customer;

import com.pos.hyper.DTO.CustomerDto;
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
                customer.getEmail(),
                customer.getIsActive(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }
    public Customer toCustomer(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setId(customerDto.id());
        customer.setName(customerDto.name());
        customer.setAddress(customerDto.address());
        customer.setPhone(customerDto.phone());
        customer.setEmail(customerDto.email());
        customer.setIsActive(customerDto.isActive());
        customer.setCreatedAt(customerDto.createdAt());
        customer.setUpdatedAt(customerDto.updatedAt());
        return customer;

    }
    public Customer toCustomer(CustomerDto customerDto, Customer customer) {
        customer.setName(customerDto.name());
        customer.setAddress(customerDto.address());
        customer.setPhone(customerDto.phone());
        customer.setEmail(customerDto.email());
        customer.setIsActive(customerDto.isActive());
        customer.setCreatedAt(customerDto.createdAt());
        customer.setUpdatedAt(customerDto.updatedAt());
        return customer;
    }
    public Customer toCustomer(CustomerDto customerDto, Customer customer, List<Invoice> invoices) {
        customer.setName(customerDto.name());
        customer.setAddress(customerDto.address());
        customer.setPhone(customerDto.phone());
        customer.setEmail(customerDto.email());
        customer.setInvoices(invoices);
        customer.setIsActive(customerDto.isActive());
        customer.setCreatedAt(customerDto.createdAt());
        customer.setUpdatedAt(customerDto.updatedAt());
        return customer;
    }

}
