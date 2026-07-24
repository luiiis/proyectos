package com.softwarelee.productos.domain.ports.out;

import com.softwarelee.productos.domain.model.Producto;

import java.util.List;
import java.util.Optional;

/**
 * PUERTO DE SALIDA: Define lo que el dominio NECESITA de la persistencia.
 *
 * El dominio NO sabe si es MySQL, PostgreSQL, MongoDB o un archivo.
 * Solo sabe que necesita guardar y buscar productos.
 *
 * La implementación real está en infrastructure/adapter/out/persistence/
 */
public interface ProductoRepository {
    List<Producto> findAll();
    Optional<Producto> findById(Long id);
    List<Producto> findByNombre(String nombre);
    List<Producto> findByCategoriaId(Long categoriaId);
    Producto save(Producto producto);
    Producto update(Producto producto);
    void softDelete(Long id);
    long count();
    List<Producto> findPaginated(int offset, int limit);
}
