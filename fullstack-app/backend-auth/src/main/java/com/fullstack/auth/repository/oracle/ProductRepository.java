package com.fullstack.auth.repository.oracle;

import com.fullstack.auth.entity.oracle.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(String category);

    List<Product> findByIsActiveTrue();

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByStockLessThan(Integer threshold);
}
