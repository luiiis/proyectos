package com.erp.repository;

import com.erp.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    @Query(value = "SELECT DATE_TRUNC('month',fecha) AS mes, COUNT(*) AS qty, SUM(total) AS monto FROM ventas WHERE estado='COMPLETADA' GROUP BY mes ORDER BY mes DESC LIMIT 12", nativeQuery = true)
    List<Object[]> reporteMensual();
}
