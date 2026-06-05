package com.fullstack.auth.repository.oracle;

import com.fullstack.auth.entity.oracle.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findByProductId(Long productId);

    List<Inventory> findByProductIdOrderByCreatedAtDesc(Long productId);
}
