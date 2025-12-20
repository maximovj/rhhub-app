package com.github.maximovj.rhhub_app.config.seeder;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.github.maximovj.rhhub_app.entity.UsuarioPermisoEstadoEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioPermisosEntity;
import com.github.maximovj.rhhub_app.repository.UsuarioPermisoEstadoRepository;
import com.github.maximovj.rhhub_app.repository.UsuarioPermisosRepository;


@Component
@Order(3)
public class UsuarioPermisosSeeder implements ApplicationRunner {

    @Autowired
    UsuarioPermisosRepository repository;

    @Autowired
    UsuarioPermisoEstadoRepository usuarioPermisoEstadoRepository;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		
        if(!repository.existsByPermisoAccion("CRUD_USUARIOS")) {

            Optional<UsuarioPermisoEstadoEntity> estado = usuarioPermisoEstadoRepository.findByEstado("ACTIVO");
            if(estado.isPresent()) {
                UsuarioPermisoEstadoEntity estado_activo = estado.get();
                repository.save(
                    UsuarioPermisosEntity.builder()
                    .permisoAccion("CRUD_USUARIOS")
                    .esPermitido(true)
                    .estado(estado_activo)
                    .build()
                );
            }
        }

	}

}
