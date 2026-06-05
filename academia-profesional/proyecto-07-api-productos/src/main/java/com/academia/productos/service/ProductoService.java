package com.academia.productos.service;

import com.academia.productos.dto.ProductoRequest;
import com.academia.productos.dto.ProductoResponse;
import com.academia.productos.entity.Producto;
import com.academia.productos.repository.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductoService {

    private final ProductoRepository repo;

    public ProductoService(ProductoRepository repo) { this.repo = repo; }

    public Page<ProductoResponse> listar(Pageable pageable) {
        return repo.findByActivoTrue(pageable).map(this::toResponse);
    }

    public ProductoResponse buscarPorId(Long id) {
        return repo.findById(id).map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
    }

    public List<ProductoResponse> buscar(String nombre) {
        return repo.findByNombreContainingIgnoreCase(nombre).stream().map(this::toResponse).toList();
    }

    public List<ProductoResponse> porCategoria(String categoria) {
        return repo.findByCategoria(categoria).stream().map(this::toResponse).toList();
    }

    public List<String> categorias() { return repo.findCategorias(); }

    public List<ProductoResponse> stockBajo(int minimo) {
        return repo.findStockBajo(minimo).stream().map(this::toResponse).toList();
    }

    @Transactional
    public ProductoResponse crear(ProductoRequest req) {
        var p = new Producto();
        p.setNombre(req.nombre());
        p.setPrecio(req.precio());
        p.setCosto(req.costo());
        p.setStock(req.stock());
        p.setSku(req.sku());
        p.setCategoria(req.categoria());
        p.setDescripcion(req.descripcion());
        return toResponse(repo.save(p));
    }

    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest req) {
        var p = repo.findById(id).orElseThrow(() -> new RuntimeException("No encontrado: " + id));
        if (req.nombre() != null) p.setNombre(req.nombre());
        if (req.precio() != null) p.setPrecio(req.precio());
        if (req.descripcion() != null) p.setDescripcion(req.descripcion());
        if (req.categoria() != null) p.setCategoria(req.categoria());
        p.setStock(req.stock());
        return toResponse(repo.save(p));
    }

    @Transactional
    public void eliminar(Long id) {
        var p = repo.findById(id).orElseThrow(() -> new RuntimeException("No encontrado: " + id));
        p.setActivo(false);
        repo.save(p);
    }

    private ProductoResponse toResponse(Producto p) {
        return new ProductoResponse(p.getId(), p.getNombre(), p.getDescripcion(),
                p.getPrecio(), p.getCosto(), p.getStock(), p.getSku(), p.getCategoria(), p.getCreatedAt());
    }
}
