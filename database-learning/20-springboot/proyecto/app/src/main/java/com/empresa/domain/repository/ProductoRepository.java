package com.empresa.domain.repository;

import com.empresa.domain.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository: Spring Data JPA genera la implementación automáticamente.
 * Solo defines la INTERFAZ con el nombre del método y Spring genera el SQL.
 */
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Spring genera: SELECT * FROM productos WHERE activo = true
    List<Producto> findByActivoTrue();

    // Con paginación: SELECT * FROM productos WHERE activo = true LIMIT ? OFFSET ?
    Page<Producto> findByActivoTrue(Pageable pageable);

    // SELECT * FROM productos WHERE nombre ILIKE '%?%'
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // SELECT * FROM productos WHERE categoria_id = ? AND precio < ?
    List<Producto> findByCategoriaIdAndPrecioLessThan(Integer categoriaId, BigDecimal precio);

    // Query personalizada con JPQL
    @Query("SELECT p FROM Producto p WHERE p.stock < :minimo AND p.activo = true")
    List<Producto> findConStockBajo(@Param("minimo") int minimo);

    // Query nativa (SQL directo de PostgreSQL)
    @Query(value = """
        SELECT p.* FROM productos p
        JOIN detalle_venta dv ON dv.producto_id = p.id
        GROUP BY p.id
        ORDER BY SUM(dv.cantidad) DESC
        LIMIT :limite
        """, nativeQuery = true)
    List<Producto> findMasVendidos(@Param("limite") int limite);
}
