package com.github.maximovj.rhhub_app.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
    name = "TBL_USUARIO_GRUPOS",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"NOMBRE", "ROL_ID", "DESCRIPCION"})
    }
)
public class UsuarioGruposEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USUARIO_GRUPO_ID", nullable = false, unique = true)
    @JsonProperty("usuario_grupo_id")
    private Long usuarioGrupoId;

    @Column(name="NOMBRE", nullable = false, unique = true)
    @JsonProperty("nombre")
    private String nombre;

    @Column(name="DESCRIPCION", nullable = false, unique = true)
    @JsonProperty("descripcion")
    private String descripcion;

    @Column(name="ES_ACTIVO", nullable = false, unique = false)
    @JsonProperty(value = "es_activo", defaultValue = "true")
    @Builder.Default
    private Boolean esActivo = false;

    // !! RELACIONES

    // Muchos grupos pertenecen a un rol
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROL_ID",  referencedColumnName = "ROL_ID")
    @Builder.Default
    private UsuarioRolEntity rol = null;

    // Un grupo puede tener muchos permisos
    @OneToMany(
        mappedBy = "grupo",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @Builder.Default
    private List<UsuarioPermisosEntity> permisos = new ArrayList<>();

    public void addPermiso(UsuarioPermisosEntity permiso) {
        permisos.add(permiso);
        permiso.setGrupo(this);
    }

    public void removePermiso(UsuarioPermisosEntity permiso) {
        permisos.remove(permiso);
        permiso.setGrupo(null);
    }

}
