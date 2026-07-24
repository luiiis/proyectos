package com.softwarelee.productos.service;

import com.softwarelee.productos.mapper.CategoriaMapper;
import com.softwarelee.productos.model.Categoria;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaMapper categoriaMapper;

    public CategoriaService(CategoriaMapper categoriaMapper) {
        this.categoriaMapper = categoriaMapper;
    }

    public List<Categoria> listarTodas() {
        return categoriaMapper.findAll();
    }

    public Categoria buscarPorId(Long id) {
        Categoria categoria = categoriaMapper.findById(id);
        if (categoria == null) {
            throw new RuntimeException("Categoría no encontrada con ID: " + id);
        }
        return categoria;
    }

    public List<Categoria> buscarPorNombre(String nombre) {
        return categoriaMapper.findByNombre(nombre);
    }

    public Categoria crear(Categoria categoria) {
        // Validar que no exista otra con el mismo nombre
        Categoria existente = categoriaMapper.findByNombreExacto(categoria.getNombre());
        if (existente != null) {
            throw new RuntimeException("Ya existe una categoría con el nombre: " + categoria.getNombre());
        }
        categoriaMapper.insert(categoria);
        return categoriaMapper.findById(categoria.getId());
    }

    public Categoria actualizar(Long id, Categoria categoria) {
        buscarPorId(id); // Valida que exista

        // Validar que no haya otra categoría con ese nombre
        Categoria existente = categoriaMapper.findByNombreExacto(categoria.getNombre());
        if (existente != null && !existente.getId().equals(id)) {
            throw new RuntimeException("Ya existe otra categoría con el nombre: " + categoria.getNombre());
        }

        categoria.setId(id);
        categoriaMapper.update(categoria);
        return categoriaMapper.findById(id);
    }

    public void eliminar(Long id) {
        buscarPorId(id); // Valida que exista
        categoriaMapper.softDelete(id);
    }

    public long contar() {
        return categoriaMapper.count();
    }
}
