package com.academia.kafka;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaEventPublisher publisher;

    public VentaController(VentaEventPublisher publisher) {
        this.publisher = publisher;
    }

    @PostMapping("/evento")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Map<String, String> publicarEvento(@RequestBody Map<String, Object> body) {
        var evento = new VentaEvent(
            ((Number) body.get("ventaId")).longValue(),
            (String) body.get("clienteEmail"),
            new BigDecimal(body.get("total").toString()),
            (List<String>) body.get("productos")
        );
        publisher.publicar(evento);
        return Map.of("status", "EVENTO_PUBLICADO", "topic", "venta-creada", "ventaId", evento.ventaId().toString());
    }
}
