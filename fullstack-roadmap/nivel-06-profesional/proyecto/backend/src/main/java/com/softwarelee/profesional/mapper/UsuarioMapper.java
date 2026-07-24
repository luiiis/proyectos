package com.softwarelee.profesional.mapper;

import com.softwarelee.profesional.model.Usuario;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UsuarioMapper {
    Usuario findByUsername(@Param("username") String username);
    Usuario findByEmail(@Param("email") String email);
    Usuario findById(@Param("id") Long id);
    void actualizarPassword(@Param("id") Long id, @Param("passwordHash") String passwordHash);
    void actualizarPerfil(@Param("id") Long id, @Param("nombre") String nombre, @Param("email") String email);
    void incrementarIntentosFallidos(@Param("id") Long id);
    void resetearIntentosFallidos(@Param("id") Long id);
    void bloquearUsuario(@Param("id") Long id, @Param("hasta") java.time.LocalDateTime hasta);
    void activarDesactivar(@Param("id") Long id, @Param("activo") boolean activo);
}
