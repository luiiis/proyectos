package com.academia.ventas.repository;

import com.academia.ventas.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.math.BigDecimal;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findTop10ByOrderByFechaDesc();

    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE MONTH(v.fecha) = MONTH(CURRENT_DATE) AND YEAR(v.fecha) = YEAR(CURRENT_DATE)")
    BigDecimal ventasDelMes();

    @Query("SELECT COUNT(v) FROM Venta v WHERE MONTH(v.fecha) = MONTH(CURRENT_DATE)")
    long pedidosDelMes();
}
