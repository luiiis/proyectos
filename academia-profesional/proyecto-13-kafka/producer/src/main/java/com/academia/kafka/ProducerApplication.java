package com.academia.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Proyecto 13: Kafka Producer
 * Publica eventos de ventas en un topic de Kafka.
 * El consumer los recibe y procesa (enviar notificación, email, etc.)
 */
@SpringBootApplication
public class ProducerApplication {
    public static void main(String[] args) { SpringApplication.run(ProducerApplication.class, args); }
}

record VentaEvento(Long ventaId, String clienteEmail, double total, List<String> productos, LocalDateTime fecha) {}

@RestController
@RequestMapping("/api/ventas")
class VentaEventController {

    private final KafkaTemplate<String, String> kafka;
    private final ObjectMapper mapper = new ObjectMapper();

    VentaEventController(KafkaTemplate<String, String> kafka) { this.kafka = kafka; }

    /**
     * POST /api/ventas/evento
     * Simula una venta y publica el evento en Kafka
     */
    @PostMapping("/evento")
    public Map<String, Object> publicarVenta(@RequestBody VentaEvento evento) throws Exception {
        // Crear evento con timestamp
        var eventoConFecha = new VentaEvento(
            evento.ventaId(), evento.clienteEmail(), evento.total(),
            evento.productos(), LocalDateTime.now()
        );

        // Serializar a JSON y publicar en topic "ventas.creada"
        String json = mapper.writeValueAsString(eventoConFecha);
        kafka.send("ventas.creada", evento.ventaId().toString(), json);

        // También publicar en topic de stock si hay productos
        if (evento.productos() != null && !evento.productos().isEmpty()) {
            kafka.send("stock.actualizado", json);
        }

        return Map.of(
            "status", "Evento publicado",
            "topic", "ventas.creada",
            "ventaId", evento.ventaId(),
            "timestamp", LocalDateTime.now().toString()
        );
    }

    /**
     * GET /api/ventas/test
     * Publica un evento de prueba rápido
     */
    @GetMapping("/test")
    public Map<String, String> test() throws Exception {
        var evento = new VentaEvento(999L, "test@mail.com", 18999.0, List.of("Laptop HP"), LocalDateTime.now());
        String json = mapper.writeValueAsString(evento);
        kafka.send("ventas.creada", "test", json);
        return Map.of("status", "Evento de prueba publicado en topic: ventas.creada");
    }
}
