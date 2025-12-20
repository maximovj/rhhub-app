package com.github.maximovj.rhhub_app.config.seeder;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.github.maximovj.rhhub_app.entity.UsuarioGruposEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioPermisosEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioRolEntity;
import com.github.maximovj.rhhub_app.repository.UsuarioPermisosRepository;
import com.github.maximovj.rhhub_app.repository.UsuarioRolRepository;

@Component
@Order(4)
public class UsuarioRolSeeder implements ApplicationRunner {

    @Autowired
    UsuarioRolRepository rolRepository;

    @Autowired
    UsuarioPermisosRepository permisosRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        UsuarioRolEntity entidad = null;
        if(rolRepository.existsByRolNombre("ADMIN")) {

            UsuarioRolEntity rolAdmin = UsuarioRolEntity.builder()
                .rolNombre("ADMIN")
                .rolEsAdministrador(false)
                .build();

            UsuarioGruposEntity grupo = UsuarioGruposEntity.builder()
            .nombre("ADMINISTRADOR")
            .descripcion("GRUPO PARA ADMINISTRADORES")
            .esActivo(true)
            .build();

            List<UsuarioPermisosEntity> permisos = permisosRepository.findByPermisoModulo("MODULO_USUARIOS");
            permisos.stream().forEach( unPermiso -> {
                grupo.addPermiso(unPermiso);
            });

            rolAdmin.addGrupo(grupo);
            rolRepository.save(rolAdmin);
        }
    }

}
