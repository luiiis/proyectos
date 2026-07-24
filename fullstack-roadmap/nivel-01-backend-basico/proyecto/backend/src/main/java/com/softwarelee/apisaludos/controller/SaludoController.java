package com.softwarelee.apisaludos.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * Controller: recibe peticiones HTTP y devuelve respuestas.
 *
 * @RestController = esta clase maneja requests HTTP y devuelve JSON
 * @RequestMapping = prefijo de la URL para todos los endpoints
 */
@RestController
@RequestMapping("/api/saludos")
public class SaludoController {

    /**
     * GET /api/saludos
     * Devuelve un saludo genérico como texto.
     */
    @GetMapping
    public String saludoGeneral() {
        return "¡Hola! Bienvenido a mi primera API";
    }

    /**
     * GET /api/saludos/{nombre}
     * Devuelve un saludo personalizado.
     */
    @GetMapping("/{nombre}")
    public String saludoPersonalizado(@PathVariable String nombre) {
        return "¡Hola " + nombre + "! Bienvenido a la API";
    }

    /**
     * GET /api/saludos/json/{nombre}
     * Devuelve un saludo en formato JSON.
     */
    @GetMapping("/json/{nombre}")
    public Map<String, Object> saludoJson(@PathVariable String nombre) {
        return Map.of(
            "mensaje", "¡Hola " + nombre + "!",
            "status", "success",
            "codigo", 200
        );
    }

    /**
     * GET /api/saludos/calculadora/sumar?a=10&b=20
     * Recibe parámetros de la URL (query params).
     */
    @GetMapping("/calculadora/sumar")
    public Map<String, Object> sumar(@RequestParam int a, @RequestParam int b) {
        int resultado = a + b;
        return Map.of(
            "operacion", a + " + " + b,
            "resultado", resultado
        );
    }

    /**
     * GET /api/saludos/hora
     * Devuelve la fecha y hora actual del servidor.
     */
    @GetMapping("/hora")
    public Map<String, String> horaActual() {
        return Map.of(
            "fecha", java.time.LocalDate.now().toString(),
            "hora", java.time.LocalTime.now().toString(),
            "zona", java.time.ZoneId.systemDefault().toString()
        );
    }
}
