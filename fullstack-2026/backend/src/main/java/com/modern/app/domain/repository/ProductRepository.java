package com.modern.app.domain.repository;

import com.modern.app.domain.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByActiveTrue();

    List<ProductEntity> findByCategory(String category);

    List<ProductEntity> findByNameContainingIgnoreCase(String name);

    Optional<ProductEntity> findBySku(String sku);

    @Query("SELECT p FROM ProductEntity p WHERE p.stock < :threshold AND p.active = true")
    List<ProductEntity> findLowStock(int threshold);

    @Query("SELECT DISTINCT p.category FROM ProductEntity p WHERE p.category IS NOT NULL")
    List<String> findAllCategories();
}
