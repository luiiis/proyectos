package com.modern.app.domain.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad Usuario - Almacenada en PostgreSQL.
 *
 * ¿Por qué NO es un Record?
 * - Las entidades JPA necesitan ser MUTABLES (JPA modifica campos internamente).
 * - Records son inmutables → no compatibles con JPA.
 * - Records se usan para DTOs (datos que viajan), no para entidades (datos que persisten).
 *
 * ¿Por qué NO usa Lombok en 2026?
 * - Java 25 tiene suficientes features para no necesitar Lombok.
 * - Lombok causa problemas con GraalVM Native Image.
 * - Los IDEs modernos generan getters/setters con un clic.
 * - Aquí escribimos los métodos explícitamente para claridad educativa.
 */
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    // El password NO se almacena aquí. Keycloak lo gestiona.
    // Este campo es solo para referencia/migración.
    @Column(name = "keycloak_id", unique = true)
    private String keycloakId;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(length = 20)
    private String phone;

    @Column(name = "is_active")
    private boolean active = true;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles = new HashSet<>();

    // ═══════ Lifecycle Callbacks ═══════
    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    // ═══════ Constructors ═══════
    protected UserEntity() {} // JPA requiere constructor sin args

    public UserEntity(String username, String email, String keycloakId) {
        this.username = username;
        this.email = email;
        this.keycloakId = keycloakId;
    }

    // ═══════ Getters & Setters ═══════
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getKeycloakId() { return keycloakId; }
    public void setKeycloakId(String keycloakId) { this.keycloakId = keycloakId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Set<RoleEntity> getRoles() { return roles; }
    public void addRole(RoleEntity role) { this.roles.add(role); }
    public void removeRole(RoleEntity role) { this.roles.remove(role); }
}
