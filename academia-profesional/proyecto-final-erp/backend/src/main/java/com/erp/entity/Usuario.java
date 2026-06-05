package com.erp.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false) private String username;
    @Column(nullable = false) private String password;
    private String email;
    private String nombre;
    private String apellido;
    @Column(name = "rol_id") private Integer rolId;
    private boolean activo = true;
    @Column(name = "created_at", updatable = false) private Instant createdAt;
    @PrePersist void onCreate() { createdAt = Instant.now(); }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        String rol = switch (rolId != null ? rolId : 3) { case 1 -> "ADMIN"; case 2 -> "GERENTE"; default -> "VENDEDOR"; };
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol));
    }
    @Override public String getUsername() { return username; }
    @Override public String getPassword() { return password; }
    @Override public boolean isEnabled() { return activo; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }

    public Long getId() { return id; }
    public void setUsername(String u) { this.username = u; }
    public void setPassword(String p) { this.password = p; }
    public String getEmail() { return email; }
    public void setEmail(String e) { this.email = e; }
    public String getNombre() { return nombre; }
    public void setNombre(String n) { this.nombre = n; }
    public String getApellido() { return apellido; }
    public void setApellido(String a) { this.apellido = a; }
    public Integer getRolId() { return rolId; }
    public void setRolId(Integer r) { this.rolId = r; }
    public Instant getCreatedAt() { return createdAt; }
}
