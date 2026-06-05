package com.empresa.controller;

import com.empresa.repository.VentaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class ReporteController {

    private final VentaRepository ventaRepository;

    public ReporteController(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @GetMapping("/ventas-mensuales")
    public List<Map<String, Object>> ventasMensuales() {
        return ventaRepository.reporteMensual().stream()
                .map(row -> Map.of(
                        "mes", row[0].toString(),
                        "cantidad", row[1],
                        "monto", row[2]
                ))
                .toList();
    }
}
