package com.softwarelee.tareas.controller;

import com.softwarelee.tareas.dto.TareaRequest;
import com.softwarelee.tareas.dto.TareaResponse;
import com.softwarelee.tareas.service.TareaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * CONTROLLER: recibe requests HTTP y delega al Service.
 *
 * Responsabilidades:
 * - Recibir parámetros de la URL/body
 * - Validar formato (@Valid)
 * - Llamar al Service
 * - Devolver respuesta HTTP con código correcto
 * - NO tiene lógica de negocio
 *
 * Códigos HTTP que usamos:
 * - 200 OK: operación exitosa
 * - 201 Created: se creó un recurso nuevo
 * - 204 No Content: se eliminó correctamente (sin body)
 * - 400 Bad Request: datos inválidos
 * - 404 Not Found: recurso no existe
 */
@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private final TareaService tareaService;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    /**
     * GET /api/tareas
     * Listar todas las tareas.
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarTodas() {
        List<TareaResponse> tareas = tareaService.listarTodas();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", tareas,
                "total", tareas.size()
        ));
    }

    /**
     * GET /api/tareas/{id}
     * Buscar una tarea por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> buscarPorId(@PathVariable Long id) {
        TareaResponse tarea = tareaService.buscarPorId(id);
        return ResponseEntity.ok(Map.of("success", true, "data", tarea));
    }

    /**
     * POST /api/tareas
     * Crear una nueva tarea.
     * @Valid activa las validaciones del DTO (@NotBlank, @Size).
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody TareaRequest request) {
        TareaResponse tarea = tareaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "message", "Tarea creada exitosamente",
                "data", tarea
        ));
    }

    /**
     * PUT /api/tareas/{id}
     * Modificar una tarea existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody TareaRequest request) {
        TareaResponse tarea = tareaService.actualizar(id, request);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Tarea actualizada",
                "data", tarea
        ));
    }

    /**
     * DELETE /api/tareas/{id}
     * Eliminar una tarea.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tareaService.eliminar(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    /**
     * PATCH /api/tareas/{id}/completar
     * Marcar una tarea como completada.
     */
    @PatchMapping("/{id}/completar")
    public ResponseEntity<Map<String, Object>> completar(@PathVariable Long id) {
        TareaResponse tarea = tareaService.completar(id);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Tarea marcada como completada",
                "data", tarea
        ));
    }
}
