package com.softwarelee.productos.mapper;

import com.softwarelee.productos.model.Categoria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * MYBATIS MAPPER: Interface para operaciones de BD sobre categorías.
 *
 * Cada método se conecta a una query definida en resources/mapper/CategoriaMapper.xml
 */
@Mapper
public interface CategoriaMapper {

    // Listar todas las categorías activas
    List<Categoria> findAll();

    // Buscar por ID
    Categoria findById(@Param("id") Long id);

    // Buscar por nombre (LIKE)
    List<Categoria> findByNombre(@Param("nombre") String nombre);

    // Insertar nueva categoría
    void insert(Categoria categoria);

    // Actualizar categoría existente
    void update(Categoria categoria);

    // Soft delete (desactivar)
    void softDelete(@Param("id") Long id);

    // Contar categorías activas
    long count();

    // Verificar si existe por nombre (para evitar duplicados)
    Categoria findByNombreExacto(@Param("nombre") String nombre);
}
