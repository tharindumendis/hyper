package com.pos.hyper.repository;

import com.pos.hyper.model.grn.Grn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrnRepository extends JpaRepository<Grn, Integer> {
}
