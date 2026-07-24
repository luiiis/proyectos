package com.softwarelee.productos.infrastructure.adapter.out.persistence;

import com.softwarelee.productos.domain.model.Producto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * MyBatis Mapper (infraestructura).
 * Solo vive en la capa de infraestructura — el dominio no lo conoce.
 */
@Mapper
public interface ProductoPersistenceMapper {
    List<Producto> findAll();
    Producto findById(@Param("id") Long id);
    List<Producto> findByNombre(@Param("nombre") String nombre);
    List<Producto> findByCategoriaId(@Param("categoriaId") Long categoriaId);
    void insert(Producto producto);
    void update(Producto producto);
    void softDelete(@Param("id") Long id);
    long count();
    List<Producto> findPaginated(@Param("offset") int offset, @Param("limit") int limit);
}
