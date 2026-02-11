package com.github.maximovj.rhhub_app.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import com.github.maximovj.rhhub_app.entity.PermisoEntity;

public class PermisoSpecBuilder extends BaseSpecification<PermisoEntity> {

    public PermisoSpecBuilder permisoId(Long permisoId) {
        if(permisoId != null) {
            super.spec = super.spec.and(equalsSpec("permisoId", permisoId));
        }
        return this;
    }

    public PermisoSpecBuilder modulo(String modulo) {
        if(modulo != null && !modulo.isBlank()) {
            super.spec = super.spec.and(likeIgnoreCase("modulo", modulo.trim()));
        }
        return this;
    }

    public PermisoSpecBuilder accion(String accion) { 
        if(accion != null && !accion.isBlank()) {
            super.spec = super.spec.and(likeIgnoreCase("accion", accion.trim()));
        }
        return this;
    }

    public Specification<PermisoEntity> build() {
        return super.spec;
    }
    
}
