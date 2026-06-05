package com.empresa.repository;

import com.empresa.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findByEstado(String estado);

    List<Venta> findByFechaBetween(Instant inicio, Instant fin);

    @Query("SELECT v FROM Venta v WHERE v.empleadoId = :empleadoId AND v.estado = 'COMPLETADA'")
    List<Venta> findByEmpleado(int empleadoId);

    @Query(value = """
        SELECT DATE_TRUNC('month', fecha) AS mes, COUNT(*) AS cantidad, SUM(total) AS monto
        FROM ventas WHERE estado = 'COMPLETADA'
        GROUP BY DATE_TRUNC('month', fecha)
        ORDER BY mes DESC LIMIT 12
        """, nativeQuery = true)
    List<Object[]> reporteMensual();
}
