package com.empresa.repository;

import com.empresa.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Page<Producto> findByActivoTrue(Pageable pageable);

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT p FROM Producto p WHERE p.stock < :minimo AND p.activo = true")
    List<Producto> findStockBajo(int minimo);

    @Query(value = """
        SELECT p.* FROM productos p
        JOIN detalle_venta dv ON dv.producto_id = p.id
        GROUP BY p.id
        ORDER BY SUM(dv.cantidad) DESC
        LIMIT :limite
        """, nativeQuery = true)
    List<Producto> findMasVendidos(int limite);
}
