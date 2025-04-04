package com.pos.hyper.controller;


import com.pos.hyper.model.Order;
import com.pos.hyper.repository.OrderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/order")
public class OrderController {

    private OrderRepository orderRepository;

    @GetMapping("")
    Iterable<Order> getOrders() {
        return orderRepository.findAll();
    }

    @PostMapping("")
    Order save(Order order) {
        return orderRepository.save(order);
    }

}
