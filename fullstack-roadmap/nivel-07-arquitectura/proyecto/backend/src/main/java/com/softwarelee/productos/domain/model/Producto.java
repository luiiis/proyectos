package com.softwarelee.productos.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ENTIDAD DE DOMINIO: Producto.
 *
 * Esta clase NO depende de ningún framework (ni Spring, ni MyBatis).
 * Es Java PURO — esto es el corazón de la Arquitectura Hexagonal.
 * Las reglas de negocio viven aquí.
 */
public class Producto {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private int existencia;
    private Long categoriaId;
    private String sku;
    private boolean activo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String categoriaNombre;

    public Producto() {}

    // ═══ Reglas de negocio (validaciones de dominio) ═══

    public void validar() {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        if (existencia < 0) {
            throw new IllegalArgumentException("La existencia no puede ser negativa");
        }
    }

    public boolean tieneStockBajo(int umbral) {
        return existencia < umbral;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public int getExistencia() { return existencia; }
    public void setExistencia(int existencia) { this.existencia = existencia; }
    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public String getCategoriaNombre() { return categoriaNombre; }
    public void setCategoriaNombre(String categoriaNombre) { this.categoriaNombre = categoriaNombre; }
}
