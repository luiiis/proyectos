package com.softwarelee.tareas.repository;

import com.softwarelee.tareas.model.Tarea;
import java.util.List;
import java.util.Optional;

/**
 * INTERFACE del repositorio.
 * Define QUÉ operaciones se pueden hacer con las tareas.
 * NO define CÓMO se hacen (eso lo hace la implementación).
 *
 * ¿Por qué una interface?
 * - Hoy los datos están en memoria (ArrayList).
 * - Mañana estarán en MySQL.
 * - El Service no cambia, solo cambia la implementación del Repository.
 */
public interface TareaRepository {

    List<Tarea> findAll();

    Optional<Tarea> findById(Long id);

    Tarea save(Tarea tarea);

    Tarea update(Tarea tarea);

    void deleteById(Long id);

    boolean existsById(Long id);
}
