package com.softwarelee.tareas.repository;

import com.softwarelee.tareas.model.Tarea;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * IMPLEMENTACIÓN del repositorio — almacena datos EN MEMORIA.
 *
 * @Repository le dice a Spring: "soy un bean de acceso a datos".
 * Spring lo crea automáticamente y lo inyecta donde se necesite.
 *
 * Usamos ConcurrentHashMap en vez de ArrayList porque:
 * - Búsqueda por ID es O(1) en vez de O(n)
 * - Es thread-safe (múltiples requests simultáneos)
 *
 * NOTA: Los datos se PIERDEN al reiniciar la app.
 * En el Nivel 3 se reemplazará por MyBatis + MySQL.
 */
@Repository
public class TareaRepositoryImpl implements TareaRepository {

    // Almacenamiento en memoria (simula una base de datos)
    private final Map<Long, Tarea> tareas = new ConcurrentHashMap<>();

    // Generador de IDs auto-incrementales (como SERIAL en PostgreSQL)
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<Tarea> findAll() {
        return new ArrayList<>(tareas.values());
    }

    @Override
    public Optional<Tarea> findById(Long id) {
        return Optional.ofNullable(tareas.get(id));
    }

    @Override
    public Tarea save(Tarea tarea) {
        Long id = idGenerator.getAndIncrement();
        tarea.setId(id);
        tareas.put(id, tarea);
        return tarea;
    }

    @Override
    public Tarea update(Tarea tarea) {
        tareas.put(tarea.getId(), tarea);
        return tarea;
    }

    @Override
    public void deleteById(Long id) {
        tareas.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return tareas.containsKey(id);
    }
}
