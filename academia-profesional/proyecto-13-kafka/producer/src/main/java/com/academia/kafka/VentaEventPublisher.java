package com.academia.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class VentaEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(VentaEventPublisher.class);
    private static final String TOPIC = "venta-creada";

    private final KafkaTemplate<String, VentaEvent> kafka;

    public VentaEventPublisher(KafkaTemplate<String, VentaEvent> kafka) {
        this.kafka = kafka;
    }

    public void publicar(VentaEvent evento) {
        kafka.send(TOPIC, evento.ventaId().toString(), evento);
        log.info("✓ Evento publicado en topic '{}': ventaId={}, total={}", TOPIC, evento.ventaId(), evento.total());
    }
}
