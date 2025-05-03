package com.pos.hyper.repository;

import com.pos.hyper.model.Stock.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Integer> {
    List<Stock> findByProductIdOrderByReceivedDateAsc(Integer productId);
     List<Stock> findByProductIdAndQuantityGreaterThanOrderByReceivedDateAsc(Integer productId, Integer quantity);

    Stock findByGrnItem_Id(Integer grnItemId);
}
