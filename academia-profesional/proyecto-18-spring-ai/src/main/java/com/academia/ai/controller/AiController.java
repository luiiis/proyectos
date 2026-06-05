package com.academia.ai.controller;

import com.academia.ai.service.ChatService;
import com.academia.ai.service.RecomendacionService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller que expone endpoints de IA.
 * Demuestra: chat con contexto, RAG, recomendaciones.
 */
@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiController {

    private final ChatService chatService;
    private final RecomendacionService recomendacionService;

    public AiController(ChatService chatService, RecomendacionService recomendacionService) {
        this.chatService = chatService;
        this.recomendacionService = recomendacionService;
    }

    /**
     * Chat simple: pregunta → respuesta de IA
     * POST /api/ai/chat {"mensaje": "¿Qué laptops tienen?"}
     */
    @PostMapping("/chat")
    public Map<String, String> chat(@RequestBody Map<String, String> request) {
        String mensaje = request.get("mensaje");
        String respuesta = chatService.responder(mensaje);
        return Map.of("pregunta", mensaje, "respuesta", respuesta);
    }

    /**
     * Chat con RAG: busca en BD + responde con contexto
     * POST /api/ai/consultar {"pregunta": "algo para trabajar desde casa"}
     */
    @PostMapping("/consultar")
    public Map<String, Object> consultar(@RequestBody Map<String, String> request) {
        String pregunta = request.get("pregunta");
        return chatService.consultarConRAG(pregunta);
    }

    /**
     * Recomendación personalizada
     * POST /api/ai/recomendar {"necesidad": "necesito algo para programar", "presupuesto": 25000}
     */
    @PostMapping("/recomendar")
    public Map<String, Object> recomendar(@RequestBody Map<String, Object> request) {
        String necesidad = (String) request.get("necesidad");
        Number presupuesto = (Number) request.get("presupuesto");
        return recomendacionService.recomendar(necesidad, presupuesto.doubleValue());
    }
}
