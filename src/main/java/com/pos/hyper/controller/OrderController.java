package com.pos.hyper.controller;


import com.pos.hyper.model.Order;
import com.pos.hyper.repository.OrderRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;


    @GetMapping("")
    List<Order> getOrders() {
        return orderRepository.findAll();
    }

    @PostMapping
    Order save(@Valid @RequestBody Order order) {
        return orderRepository.save(order);
    }

    @PutMapping("/{id}")
    Order update(@Valid @RequestBody Order order, @PathVariable Long id) {
        Order ord = orderRepository.findById(id).get();
        ord.setProductId(order.getProductId());
        ord.setQuantity(order.getQuantity());
        ord.setUnitPrice(order.getUnitPrice());
        ord.setDiscount(order.getDiscount());
        ord.setCostPrice(order.getCostPrice());
        ord.setAmount(order.getAmount());
        return orderRepository.save(ord);
    }
    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        orderRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }



}
