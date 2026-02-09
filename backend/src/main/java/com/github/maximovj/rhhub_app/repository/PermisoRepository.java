package com.github.maximovj.rhhub_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.maximovj.rhhub_app.entity.PermisoEntity;

import java.util.Collection;
import java.util.List;


public interface PermisoRepository extends JpaRepository<PermisoEntity, Long> {

    boolean existsByPermisoAccion(String permisoAccion);

    Optional<PermisoEntity> findByPermisoAccion(String permisoAccion);

    List<PermisoEntity> findByPermisoModulo(String permisoModulo);

    List<PermisoEntity> findByPermisoAccionIn(Collection<String> permisoAccion);

    List<PermisoEntity> findByPermisoModuloIn(Collection<String> permisoModulo);

}
