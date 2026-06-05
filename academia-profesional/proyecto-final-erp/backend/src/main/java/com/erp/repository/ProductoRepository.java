package com.erp.repository;

import com.erp.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Page<Producto> findByActivoTrue(Pageable pageable);
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    @Query("SELECT p FROM Producto p WHERE p.stock < :min AND p.activo = true")
    List<Producto> findStockBajo(int min);
}
