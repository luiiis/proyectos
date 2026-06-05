package com.erp.controller;

import com.erp.entity.Venta;
import com.erp.service.VentaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {
    private final VentaService service;
    public VentaController(VentaService s) { this.service = s; }

    @GetMapping
    public List<Venta> listar() { return service.listar(); }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Venta registrar(@RequestBody Map<String, Object> body) {
        Integer clienteId = (Integer) body.get("clienteId");
        Integer usuarioId = (Integer) body.get("usuarioId");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = (List<Map<String, Object>>) body.get("productos");
        return service.registrar(clienteId, usuarioId, items);
    }

    @GetMapping("/reporte-mensual")
    public List<Map<String, Object>> reporteMensual() {
        return service.reporteMensual().stream()
                .map(r -> Map.of("mes", r[0].toString(), "cantidad", r[1], "monto", r[2]))
                .toList();
    }
}
