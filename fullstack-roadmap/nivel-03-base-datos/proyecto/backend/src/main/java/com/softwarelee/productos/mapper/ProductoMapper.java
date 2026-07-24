package com.softwarelee.productos.mapper;

import com.softwarelee.productos.model.Producto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * MYBATIS MAPPER: Interface que define las operaciones de BD.
 *
 * @Mapper le dice a MyBatis: "genera una implementación de esta interface".
 * Las queries SQL están en el archivo XML: resources/mapper/ProductoMapper.xml
 *
 * ¿Por qué XML y no anotaciones?
 * - Queries complejas (JOIN, paginación) son más legibles en XML
 * - Puedes cambiar el SQL sin recompilar
 * - Es el estándar en proyectos enterprise
 */
@Mapper
public interface ProductoMapper {

    // Listar todos los productos activos (con nombre de categoría)
    List<Producto> findAll();

    // Buscar por ID
    Producto findById(@Param("id") Long id);

    // Buscar por nombre (LIKE)
    List<Producto> findByNombre(@Param("nombre") String nombre);

    // Filtrar por categoría
    List<Producto> findByCategoriaId(@Param("categoriaId") Long categoriaId);

    // Productos con existencia baja
    List<Producto> findLowStock(@Param("minimo") int minimo);

    // Insertar
    void insert(Producto producto);

    // Actualizar
    void update(Producto producto);

    // Eliminar (soft delete: activo = false)
    void softDelete(@Param("id") Long id);

    // Contar total
    long count();

    // Paginación
    List<Producto> findPaginated(@Param("offset") int offset, @Param("limit") int limit);
}
