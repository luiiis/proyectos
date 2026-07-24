package com.softwarelee.productos.infrastructure.adapter.out.persistence;

import com.softwarelee.productos.domain.model.Producto;
import com.softwarelee.productos.domain.ports.out.ProductoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * ADAPTADOR DE SALIDA (Persistencia): Implementa el puerto usando MyBatis.
 *
 * Este adaptador traduce las llamadas del dominio a operaciones de BD.
 * Si mañana cambias a JPA o MongoDB, solo cambias este archivo.
 * El dominio NO se entera del cambio.
 */
@Repository
public class ProductoMyBatisAdapter implements ProductoRepository {

    private final ProductoPersistenceMapper mapper;

    public ProductoMyBatisAdapter(ProductoPersistenceMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<Producto> findAll() {
        return mapper.findAll();
    }

    @Override
    public Optional<Producto> findById(Long id) {
        return Optional.ofNullable(mapper.findById(id));
    }

    @Override
    public List<Producto> findByNombre(String nombre) {
        return mapper.findByNombre(nombre);
    }

    @Override
    public List<Producto> findByCategoriaId(Long categoriaId) {
        return mapper.findByCategoriaId(categoriaId);
    }

    @Override
    public Producto save(Producto producto) {
        mapper.insert(producto);
        return mapper.findById(producto.getId());
    }

    @Override
    public Producto update(Producto producto) {
        mapper.update(producto);
        return mapper.findById(producto.getId());
    }

    @Override
    public void softDelete(Long id) {
        mapper.softDelete(id);
    }

    @Override
    public long count() {
        return mapper.count();
    }

    @Override
    public List<Producto> findPaginated(int offset, int limit) {
        return mapper.findPaginated(offset, limit);
    }
}
