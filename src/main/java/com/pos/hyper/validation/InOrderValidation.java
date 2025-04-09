package com.pos.hyper.validation;

import com.pos.hyper.model.inOrder.InOrder;
import com.pos.hyper.model.product.Product;
import com.pos.hyper.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class InOrderValidation {
    Product product;



    private final ProductRepository productRepository;

    public InOrderValidation(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<InOrder> inOrdersValidate(List<InOrder> inOrders) {
        List<InOrder> newInOrdersSet = new ArrayList<>();
        for (InOrder inOrder : inOrders) {
            newInOrdersSet.add(inOrderValidate(inOrder));
        }
        return newInOrdersSet;
    }

    public InOrder inOrderValidate(InOrder inOrder) {
        setProduct(inOrder);
        validateInOrder(inOrder);
        return inOrder;
    }
    public void validateInOrder(InOrder InOrder) {
        List<String> errors = new ArrayList<>();
        if (!invoiceValidation(InOrder)) errors.add("Invoice validation failed");
        if (!productValidation()) errors.add("Product validation failed");
        if (!priceValidation(InOrder)) errors.add("Price validation failed");
        if (!quantityValidation(InOrder)) errors.add("Quantity validation failed");
        if (!costValidation(InOrder)) errors.add("Cost validation failed");
        if (!amountValidation(InOrder)) errors.add("Amount validation failed");

        if (!errors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "P.Id : " + InOrder.getProduct().getId() + " " + String.join(", ", errors));
        }
    }

    public Object discountValidation(InOrder inOrder){
        if(inOrder.getDiscount() > 100 || inOrder.getDiscount() < 0){
            throw new RuntimeException("Discount cannot be greater than 100% or less than 0%");
        }
        return inOrder.getDiscount() == 0 ? null : inOrder.getDiscount();
    }

    public Boolean invoiceValidation(InOrder inOrder){
    return true;
        //return invoiceRepository.existsById(order.getInvoice().getId());
    }

    public Boolean productValidation(){
        return product != null;
    }

    public Boolean priceValidation(InOrder inOrder){
        return Objects.equals(product.getPrice(), inOrder.getUnitPrice());
    }

    public Boolean costValidation(InOrder inOrder){
        return Objects.equals(product.getCost(), inOrder.getCostPrice());
    }

    public Boolean quantityValidation(InOrder inOrder){
        return (product.getQuantity() >= inOrder.getQuantity());
    }

    public Boolean amountValidation(InOrder inOrder) {
        double discountFactor = 1 - ((double) inOrder.getDiscount() / 100);
        return inOrder.getAmount() == (inOrder.getUnitPrice() * inOrder.getQuantity() * discountFactor);
    }

    public void setProduct(InOrder inOrder) {
        this.product = productRepository.findById(inOrder.getProduct().getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found")
        );
    }

}
