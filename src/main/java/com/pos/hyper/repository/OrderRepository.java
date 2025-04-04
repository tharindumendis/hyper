package com.pos.hyper.repository;

import com.pos.hyper.model.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order,Integer> {
}
