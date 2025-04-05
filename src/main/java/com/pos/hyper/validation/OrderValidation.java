package com.pos.hyper.validation;

import com.pos.hyper.model.Order;
import com.pos.hyper.model.Product;
import com.pos.hyper.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class OrderValidation {

    Product product;

    @Autowired
    private ProductRepository productRepository;

    public List<Order> ordersValidate(List<Order> orders) {
        List<Order> newOrdersSet = new ArrayList<>();
        for (Order order : orders) {
            newOrdersSet.add(orderValidate(order));
        }
        return newOrdersSet;
    }

    public Order orderValidate(Order order) {
        setProduct(order);
        Long productId = order.getProductId();
        if(!invoiceValidation(order)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "P.Id : "+productId+" Invoice validation failed");
        }
        if(!productValidation(order)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "P.Id : "+productId+" Product validation failed");
        }
        if(!priceValidation(order)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "P.Id : "+productId+" Price validation failed");
        }
        if(!quantityValidation(order)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "P.Id : "+productId+" Quantity validation failed");
        }
        if(!costValidation(order)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "P.Id : "+productId+" Cost validation failed");
        }
        if(!amountValidation(order)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "P.Id : "+productId+" Amount validation failed");
        }
        return order;
    }

    public Object discountValidation(Order order){
        if(order.getDiscount() > 100 && order.getDiscount() < 0){
            throw new RuntimeException("Discount cannot be greater than 100% or less than 0%");
        } else if (order.getDiscount() == 0) {
            return null;
        }
        return order.getDiscount();
    }

    public Boolean invoiceValidation(Order order){
        return productRepository.findById(order.getInvoiceId()).isPresent();
    }

    public Boolean productValidation(Order order){
        return productRepository.findById(order.getProductId()).isPresent();
    }

    public Boolean priceValidation(Order order){
        return Objects.equals(product.getPrice(), order.getUnitPrice());
    }

    public Boolean costValidation(Order order){
        return Objects.equals(product.getCost(), order.getCostPrice());
    }

    public Boolean quantityValidation(Order order){
        return (product.getQuantity() >= order.getQuantity());
    }

    public Boolean amountValidation(Order order){
        if(discountValidation(order) != null){
            return order.getAmount() == (order.getUnitPrice() * order.getQuantity() * (1 - ((double) order.getDiscount() / 100)));
        }
        return order.getAmount() == (order.getUnitPrice() * order.getQuantity());
    }












    public Product getProduct() {
        return product;
    }

    public void setProduct(Order order) {
        this.product = productRepository.findById(order.getProductId()).get();
    }
}
