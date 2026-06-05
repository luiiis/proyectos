package com.academia.apiclientes.model;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Modelo de dominio (sin JPA por ahora - almacenamiento en memoria).
 * En el proyecto 07 se conectará a PostgreSQL con JPA.
 */
public class Cliente {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String ciudad;
    private boolean activo;
    private Instant createdAt;

    public Cliente() {
        this.id = ID_GENERATOR.getAndIncrement();
        this.activo = true;
        this.createdAt = Instant.now();
    }

    public Cliente(String nombre, String apellido, String email, String telefono, String ciudad) {
        this();
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.ciudad = ciudad;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public Instant getCreatedAt() { return createdAt; }
}
