package com.pos.hyper.validation;

import com.pos.hyper.model.Order;
import com.pos.hyper.model.Product;
import com.pos.hyper.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class OrderValidation {

    Product product;


    private final ProductRepository productRepository;

    public OrderValidation(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Order> ordersValidate(List<Order> orders) {
        List<Order> newOrdersSet = new ArrayList<>();
        for (Order order : orders) {
            newOrdersSet.add(orderValidate(order));
        }
        return newOrdersSet;
    }

    public Order orderValidate(Order order) {
        setProduct(order);
        validateOrder(order);
        return order;
    }
    public void validateOrder(Order order) {
        List<String> errors = new ArrayList<>();
        if (!invoiceValidation(order)) errors.add("Invoice validation failed");
        if (!productValidation(order)) errors.add("Product validation failed");
        if (!priceValidation(order)) errors.add("Price validation failed");
        if (!quantityValidation(order)) errors.add("Quantity validation failed");
        if (!costValidation(order)) errors.add("Cost validation failed");
        if (!amountValidation(order)) errors.add("Amount validation failed");

        if (!errors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "P.Id : " + order.getProductId() + " " + String.join(", ", errors));
        }
    }

    public Object discountValidation(Order order){
        if(order.getDiscount() > 100 || order.getDiscount() < 0){
            throw new RuntimeException("Discount cannot be greater than 100% or less than 0%");
        }
        return order.getDiscount() == 0 ? null : order.getDiscount();
    }

    public Boolean invoiceValidation(Order order){
        return productRepository.findById(order.getInvoiceId()).isPresent();
    }

    public Boolean productValidation(Order order){
        return product != null;
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

    public Boolean amountValidation(Order order) {
        double discountFactor = 1 - ((double) order.getDiscount() / 100);
        return order.getAmount() == (order.getUnitPrice() * order.getQuantity() * discountFactor);
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Order order) {
        this.product = productRepository.findById(order.getProductId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found")
        );
    }
}
