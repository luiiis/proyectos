package com.academia.ventas.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
public class Venta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String numero;

    private LocalDateTime fecha = LocalDateTime.now();

    @ManyToOne @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private BigDecimal subtotal = BigDecimal.ZERO;
    private BigDecimal impuesto = BigDecimal.ZERO;
    private BigDecimal total = BigDecimal.ZERO;
    private String estado = "COMPLETADA";

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles = new ArrayList<>();

    public Long getId() { return id; }
    public String getNumero() { return numero; }
    public void setNumero(String n) { this.numero = n; }
    public LocalDateTime getFecha() { return fecha; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente c) { this.cliente = c; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario u) { this.usuario = u; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal s) { this.subtotal = s; }
    public BigDecimal getImpuesto() { return impuesto; }
    public void setImpuesto(BigDecimal i) { this.impuesto = i; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal t) { this.total = t; }
    public String getEstado() { return estado; }
    public List<DetalleVenta> getDetalles() { return detalles; }
    public void addDetalle(DetalleVenta d) { detalles.add(d); d.setVenta(this); }
}
