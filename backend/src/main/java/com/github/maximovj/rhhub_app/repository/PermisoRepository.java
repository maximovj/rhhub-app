package com.github.maximovj.rhhub_app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.maximovj.rhhub_app.entity.PermisoEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioEntity;
import com.github.maximovj.rhhub_app.projection.UsuarioProjection;
import com.github.maximovj.rhhub_app.service.integration.JpaBaseRepository;

import java.util.Collection;
import java.util.List;


public interface PermisoRepository extends JpaBaseRepository<PermisoEntity, Long> {

    boolean existsByAccionAndModulo(String accion, String modulo);

    boolean existsByAccion(String accion);

    @EntityGraph(attributePaths = {"grupos"})
    @Query("SELECT p FROM PermisoEntity p WHERE (:permisoId IS NOT NULL AND p.permisoId = :permisoId)")
    Optional<PermisoEntity> qBuscaPorIdCargaRelaciones(
        @Param("permisoId") Long permisoId
    );

    Optional<PermisoEntity> findByAccion(String accion);

    List<PermisoEntity> findByModulo(String modulo);

    List<PermisoEntity> findByAccionIn(Collection<String> accion);

    List<PermisoEntity> findByModuloIn(Collection<String> modulo);

}
