package com.pos.hyper.controller;


import com.pos.hyper.model.Order;
import com.pos.hyper.repository.OrderRepository;
import com.pos.hyper.validation.OrderValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderValidation orderValidation;

    public OrderController(OrderRepository orderRepository, OrderValidation orderValidation) {
        this.orderRepository = orderRepository;
        this.orderValidation = orderValidation;
    }


    @GetMapping("")
    List<Order> getOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    Order getOrder(@PathVariable Long id) {
        return orderRepository.findById(id).get();
    }
    @PostMapping("")
    Order save(@Valid @RequestBody Order order) {
        order = orderValidation.orderValidate(order);
        return orderRepository.save(order);
    }

    @PutMapping("/{id}")
    Order update(@Valid @RequestBody Order order, @PathVariable Long id) {
        if (orderRepository.existsById(id)) {
            order.setId(id);
            order = orderValidation.orderValidate(order);
            return orderRepository.save(order);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "P.Id : "+id+" Not Found");
        }
    }
    @PutMapping("")
    ResponseEntity<?> update(@RequestBody Order order) {
        order = orderValidation.orderValidate(order);
        orderRepository.save(order);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        orderRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
