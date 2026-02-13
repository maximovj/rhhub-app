package com.github.maximovj.rhhub_app.dto.records;

import com.github.maximovj.rhhub_app.entity.UsuarioEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioEstadoEntity;

public record UsuarioDTO(
    Long usuario_id,
    String usuario,
    String correo,
    String token,
    GrupoDTO grupo,
    UsuarioEstadoEntity estado) {

    public UsuarioDTO(UsuarioEntity e) {
        this(e.getUsuarioId(),
            e.getUsuario(),
            e.getCorreo(),
            e.getToken(),
            new GrupoDTO(e.getGrupo()),
            e.getEstado());
    }

    public UsuarioDTO(UsuarioEntity e, GrupoDTO grupo, UsuarioEstadoEntity estado) {
        this(e.getUsuarioId(),
            e.getUsuario(),
            e.getCorreo(),
            e.getToken(),
            grupo,
            estado);
    }

}