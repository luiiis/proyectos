package com.erp.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "productos")
public class Producto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String nombre;
    private String descripcion;
    @Column(nullable = false) private BigDecimal precio;
    private BigDecimal costo;
    private int stock;
    @Column(unique = true) private String sku;
    @Column(name = "categoria_id") private Integer categoriaId;
    @Column(name = "proveedor_id") private Integer proveedorId;
    private boolean activo = true;
    @Column(name = "created_at", updatable = false) private Instant createdAt;
    @PrePersist void onCreate() { createdAt = Instant.now(); }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String n) { this.nombre = n; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String d) { this.descripcion = d; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal p) { this.precio = p; }
    public BigDecimal getCosto() { return costo; }
    public void setCosto(BigDecimal c) { this.costo = c; }
    public int getStock() { return stock; }
    public void setStock(int s) { this.stock = s; }
    public String getSku() { return sku; }
    public void setSku(String s) { this.sku = s; }
    public Integer getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Integer c) { this.categoriaId = c; }
    public Integer getProveedorId() { return proveedorId; }
    public void setProveedorId(Integer p) { this.proveedorId = p; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean a) { this.activo = a; }
    public Instant getCreatedAt() { return createdAt; }

    public void addStock(int qty) { if (qty <= 0) throw new IllegalArgumentException("Cantidad positiva"); this.stock += qty; }
    public void removeStock(int qty) { if (qty > stock) throw new IllegalStateException("Stock insuficiente: " + stock); this.stock -= qty; }
}
