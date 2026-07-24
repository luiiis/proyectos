package com.softwarelee.productos.service;

import com.softwarelee.productos.mapper.ProductoMapper;
import com.softwarelee.productos.model.Producto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductoService {

    private final ProductoMapper productoMapper;

    public ProductoService(ProductoMapper productoMapper) {
        this.productoMapper = productoMapper;
    }

    public List<Producto> listarTodos() {
        return productoMapper.findAll();
    }

    public Producto buscarPorId(Long id) {
        Producto producto = productoMapper.findById(id);
        if (producto == null) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        return producto;
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productoMapper.findByNombre(nombre);
    }

    public List<Producto> buscarPorCategoria(Long categoriaId) {
        return productoMapper.findByCategoriaId(categoriaId);
    }

    public List<Producto> stockBajo(int minimo) {
        return productoMapper.findLowStock(minimo);
    }

    public Producto crear(Producto producto) {
        productoMapper.insert(producto);
        return productoMapper.findById(producto.getId());
    }

    public Producto actualizar(Long id, Producto producto) {
        buscarPorId(id);
        producto.setId(id);
        productoMapper.update(producto);
        return productoMapper.findById(id);
    }

    public void eliminar(Long id) {
        buscarPorId(id);
        productoMapper.softDelete(id);
    }

    public Map<String, Object> listarPaginado(int page, int size) {
        int offset = page * size;
        List<Producto> productos = productoMapper.findPaginated(offset, size);
        long total = productoMapper.count();
        int totalPages = (int) Math.ceil((double) total / size);

        return Map.of(
            "content", productos,
            "page", page,
            "size", size,
            "totalElements", total,
            "totalPages", totalPages
        );
    }
}
