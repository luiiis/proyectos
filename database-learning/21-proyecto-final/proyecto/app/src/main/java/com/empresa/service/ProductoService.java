package com.empresa.service;

import com.empresa.entity.Producto;
import com.empresa.repository.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public Page<Producto> listar(Pageable pageable) {
        return repository.findByActivoTrue(pageable);
    }

    public Producto buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
    }

    public List<Producto> buscar(String nombre) {
        return repository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Producto> stockBajo(int minimo) {
        return repository.findStockBajo(minimo);
    }

    public List<Producto> masVendidos(int limite) {
        return repository.findMasVendidos(limite);
    }

    @Transactional
    public Producto crear(Producto producto) {
        return repository.save(producto);
    }

    @Transactional
    public Producto actualizar(Long id, Producto datos) {
        var producto = buscarPorId(id);
        if (datos.getNombre() != null) producto.setNombre(datos.getNombre());
        if (datos.getPrecio() != null) producto.setPrecio(datos.getPrecio());
        if (datos.getDescripcion() != null) producto.setDescripcion(datos.getDescripcion());
        if (datos.getStock() != null) producto.setStock(datos.getStock());
        return repository.save(producto);
    }

    @Transactional
    public void eliminar(Long id) {
        var producto = buscarPorId(id);
        producto.setActivo(false);
        repository.save(producto);
    }
}
