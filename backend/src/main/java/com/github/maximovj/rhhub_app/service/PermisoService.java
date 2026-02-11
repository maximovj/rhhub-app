package com.github.maximovj.rhhub_app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.github.maximovj.rhhub_app.entity.PermisoEntity;
import com.github.maximovj.rhhub_app.repository.PermisoRepository;
import com.github.maximovj.rhhub_app.service.integration.BaseServiceImpl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PermisoService extends BaseServiceImpl<PermisoEntity, Long, PermisoRepository> {

    protected PermisoService(PermisoRepository jpaBaseRepository) {
        super(jpaBaseRepository);
    }

    public boolean estoExiste(String accion, String modulo) {
        return this.jpaBaseRepository.existsByAccionAndModulo(accion, modulo);
    }

    

}
