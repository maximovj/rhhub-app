package com.github.maximovj.rhhub_app.dto.autenticacion;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.maximovj.rhhub_app.entity.PermisosEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfoUsuarioOutDto {
    
    private String usuario;
    
    @JsonProperty(value = "usuario_id")
    private long usuarioId;

    private String grupo;

    private String rol;

    @JsonProperty(value = "es_admin")
    private boolean esAdmin;

    private Set<PermisosEntity> permisos;

    private boolean recuerdame;

}
