package com.pos.hyper.controller;


import com.pos.hyper.model.Order;
import com.pos.hyper.repository.OrderRepository;
import com.pos.hyper.validation.OrderValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderValidation orderValidation;


    @GetMapping("")
    List<Order> getOrders() {
        return orderRepository.findAll();
    }

    @PostMapping("")
    Order save(@Valid @RequestBody Order order) {
        order = orderValidation.orderValidate(order);
        return orderRepository.save(order);
    }

    @PutMapping("/{id}")
    Order update(@Valid @RequestBody Order order, @PathVariable Long id) {
        order = orderValidation.orderValidate(order);
        Order ord = orderRepository.findById(id).get();
        return orderRepository.save(ord);
    }
    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        orderRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
