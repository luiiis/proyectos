package com.softwarelee.productos.mapper;

import com.softwarelee.productos.model.Producto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductoMapper {
    List<Producto> findAll();
    Producto findById(@Param("id") Long id);
    List<Producto> findByNombre(@Param("nombre") String nombre);
    List<Producto> findByCategoriaId(@Param("categoriaId") Long categoriaId);
    List<Producto> findLowStock(@Param("minimo") int minimo);
    void insert(Producto producto);
    void update(Producto producto);
    void softDelete(@Param("id") Long id);
    long count();
    List<Producto> findPaginated(@Param("offset") int offset, @Param("limit") int limit);
}
