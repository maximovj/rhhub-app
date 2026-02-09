package com.github.maximovj.rhhub_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.maximovj.rhhub_app.entity.UsuarioRolEntity;

public interface UsuarioRolRepository extends JpaRepository<UsuarioRolEntity, Long> { 

    boolean existsByNombre(String Nombre);

    Optional<UsuarioRolEntity> findByNombre(String Nombre);

    @EntityGraph(attributePaths = {"grupos"})
    @Query("SELECT r FROM UsuarioRolEntity r WHERE (:rolId IS NOT NULL AND r.rolId = :rolId)")
    Optional<UsuarioRolEntity> qBuscarPorIdCargaRelaciones(
        @Param("rolId") Long rolId
    );

}
