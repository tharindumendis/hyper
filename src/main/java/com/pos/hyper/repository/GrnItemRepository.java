package com.pos.hyper.repository;

import com.pos.hyper.model.grn_item.GRNItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GrnItemRepository extends JpaRepository<GRNItem, Integer> {
    List<GRNItem> findAllByGrn_Id(Integer id);

    @Query(value = "SELECT SUM(amount) AS total_value FROM grnitem WHERE grn_id = :id", nativeQuery = true)
    Double getTotalByGRN(Integer id);
}
