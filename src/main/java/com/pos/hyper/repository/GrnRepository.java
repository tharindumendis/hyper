package com.pos.hyper.repository;

import com.pos.hyper.model.grn.Grn;
import com.pos.hyper.model.inventory.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;

public interface GrnRepository extends JpaRepository<Grn, Integer> {
    List<Grn> findAllByInventoryId(Integer id);

    @Query(value = "SELECT SUM(amount) AS total_value FROM grn WHERE inventory_id = :id", nativeQuery = true)
    Double getTotalByInventory(Integer id);
}
