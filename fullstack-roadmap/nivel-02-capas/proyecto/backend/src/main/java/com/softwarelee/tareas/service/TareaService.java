package com.softwarelee.tareas.service;

import com.softwarelee.tareas.dto.TareaRequest;
import com.softwarelee.tareas.dto.TareaResponse;
import com.softwarelee.tareas.exception.TareaNotFoundException;
import com.softwarelee.tareas.model.Tarea;
import com.softwarelee.tareas.repository.TareaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * SERVICE: contiene la LÓGICA DE NEGOCIO.
 *
 * Responsabilidades:
 * - Validar reglas de negocio
 * - Convertir DTO ↔ Model
 * - Orquestar operaciones
 * - NO sabe de HTTP (eso es del Controller)
 * - NO sabe de SQL (eso es del Repository)
 *
 * @Service le dice a Spring: "soy un bean de lógica de negocio".
 */
@Service
public class TareaService {

    private final TareaRepository tareaRepository;

    // Constructor injection (Spring inyecta el Repository automáticamente)
    public TareaService(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    public List<TareaResponse> listarTodas() {
        return tareaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public TareaResponse buscarPorId(Long id) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new TareaNotFoundException("Tarea no encontrada con ID: " + id));
        return toResponse(tarea);
    }

    public TareaResponse crear(TareaRequest request) {
        Tarea tarea = new Tarea(null, request.titulo(), request.descripcion());
        Tarea guardada = tareaRepository.save(tarea);
        return toResponse(guardada);
    }

    public TareaResponse actualizar(Long id, TareaRequest request) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new TareaNotFoundException("Tarea no encontrada con ID: " + id));

        tarea.setTitulo(request.titulo());
        tarea.setDescripcion(request.descripcion());
        tarea.setFechaActualizacion(LocalDateTime.now());

        Tarea actualizada = tareaRepository.update(tarea);
        return toResponse(actualizada);
    }

    public void eliminar(Long id) {
        if (!tareaRepository.existsById(id)) {
            throw new TareaNotFoundException("Tarea no encontrada con ID: " + id);
        }
        tareaRepository.deleteById(id);
    }

    public TareaResponse completar(Long id) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new TareaNotFoundException("Tarea no encontrada con ID: " + id));

        tarea.setCompletada(true);
        tarea.setFechaActualizacion(LocalDateTime.now());

        Tarea actualizada = tareaRepository.update(tarea);
        return toResponse(actualizada);
    }

    // Mapper: Model → DTO Response
    private TareaResponse toResponse(Tarea tarea) {
        return new TareaResponse(
                tarea.getId(),
                tarea.getTitulo(),
                tarea.getDescripcion(),
                tarea.isCompletada(),
                tarea.getFechaCreacion(),
                tarea.getFechaActualizacion()
        );
    }
}
