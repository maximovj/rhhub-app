**La mejor relación depende del CASO DE USO**, pero te explico las opciones:

## **Opción 1: `UsuarioEntity` → `UsuarioRolEntity` (ManyToOne) - RECOMENDADA**

```java
// UsuarioEntity.java
@Entity
@Table(name = "TBL_USUARIOS")
public class UsuarioEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usuarioId;
    
    private String nombre;
    private String email;
    
    // Muchos usuarios pueden tener un mismo rol
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROL_ID", nullable = false)
    @JsonIgnoreProperties({"usuarios", "hibernateLazyInitializer", "handler"})
    private UsuarioRolEntity rol;
    
    // ... otros campos
}

// UsuarioRolEntity.java  
@Entity
@Table(name = "TBL_USUARIO_ROLES")
public class UsuarioRolEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rolId;
    
    private String rolNombre;
    private String rolDescripcion;
    
    // Un rol puede tener muchos usuarios
    @OneToMany(mappedBy = "rol", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"rol", "hibernateLazyInitializer", "handler"})
    @Builder.Default
    private List<UsuarioEntity> usuarios = new ArrayList<>();
    
    // Método helper
    public void addUsuario(UsuarioEntity usuario) {
        usuarios.add(usuario);
        usuario.setRol(this);
    }
    
    public void removeUsuario(UsuarioEntity usuario) {
        usuarios.remove(usuario);
        usuario.setRol(null);
    }
}
```

### **Ventajas:**
✅ **Más natural**: "Un usuario TIENE UN rol"  
✅ **Mejor rendimiento**: Foreign key en tabla usuarios (más consultas)  
✅ **Menos código**: Relación simple  
✅ **Fácil consultas**: `WHERE usuario.rol_id = ?`  
✅ **Índices eficientes**: Clave foránea única  

## **Opción 2: `UsuarioRolEntity` → `List<UsuarioEntity>` (OneToMany con tabla intermedia)**

```java
// UsuarioEntity.java
@Entity
public class UsuarioEntity {
    
    @Id
    private Long usuarioId;
    
    private String nombre;
    
    @ManyToMany(mappedBy = "usuarios", fetch = FetchType.LAZY)
    @JsonIgnore
    @Builder.Default
    private Set<UsuarioRolEntity> roles = new HashSet<>();  // Set si puede tener múltiples roles
    
    // ... otros campos
}

// UsuarioRolEntity.java
@Entity
public class UsuarioRolEntity {
    
    @Id
    private Long rolId;
    
    private String rolNombre;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "USUARIO_ROLES_ASIGNACION",
        joinColumns = @JoinColumn(name = "ROL_ID"),
        inverseJoinColumns = @JoinColumn(name = "USUARIO_ID")
    )
    @JsonIgnoreProperties({"roles", "hibernateLazyInitializer", "handler"})
    @Builder.Default
    private Set<UsuarioEntity> usuarios = new HashSet<>();
}
```

### **Ventajas (solo si usuarios pueden tener MÚLTIPLES roles):**
✅ **Flexibilidad**: Usuarios con múltiples roles  
✅ **Escalable**: Añadir más roles fácilmente  
✅ **Relación N-N**: Para sistemas complejos  

## **Opción 3: Tabla intermedia explícita (para datos adicionales)**

```java
// UsuarioRolAsignacionEntity.java
@Entity
@Table(name = "USUARIO_ROL_ASIGNACION")
public class UsuarioRolAsignacionEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "USUARIO_ID")
    private UsuarioEntity usuario;
    
    @ManyToOne
    @JoinColumn(name = "ROL_ID")
    private UsuarioRolEntity rol;
    
    @Column(name = "FECHA_ASIGNACION")
    private LocalDateTime fechaAsignacion;
    
    @Column(name = "ASIGNADO_POR")
    private String asignadoPor;
    
    @Column(name = "ES_ACTIVO")
    private Boolean esActivo;
}

// UsuarioEntity.java
@Entity
public class UsuarioEntity {
    
    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private List<UsuarioRolAsignacionEntity> rolAsignaciones = new ArrayList<>();
    
    // Método para obtener roles activos
    public List<UsuarioRolEntity> getRolesActivos() {
        return rolAsignaciones.stream()
            .filter(ra -> Boolean.TRUE.equals(ra.getEsActivo()))
            .map(UsuarioRolAsignacionEntity::getRol)
            .collect(Collectors.toList());
    }
}
```

### **Ventajas:**
✅ **Auditoría completa**: Fechas, quién asignó, estado  
✅ **Historial**: Ver cambios de roles  
✅ **Atributos adicionales**: Razón, vigencia, etc.  
✅ **Mejor para sistemas empresariales**  

## **Mi recomendación POR ORDEN:**

### **1. Si usuarios tienen UN SOLO rol → Opción 1 (ManyToOne)**
```java
// La MÁS común y recomendada para la mayoría de sistemas
@ManyToOne
@JoinColumn(name = "ROL_ID")
private UsuarioRolEntity rol;
```

### **2. Si usuarios pueden tener MÚLTIPLES roles → Opción 2 (ManyToMany)**
```java
@ManyToMany
@JoinTable(...)
private Set<UsuarioRolEntity> roles = new HashSet<>();
```

### **3. Si necesitas auditoría/historial → Opción 3 (tabla intermedia explícita)**

## **Ejemplo COMPLETO con Opción 1 (recomendada):**

### **UsuarioEntity.java**
```java
package com.github.maximovj.rhhub_app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TBL_USUARIOS")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USUARIO_ID")
    private Long usuarioId;

    @Column(name = "NOMBRE", nullable = false)
    private String nombre;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "ES_ACTIVO", nullable = false)
    @Builder.Default
    private Boolean esActivo = true;

    // !! RELACIÓN CON ROL (ManyToOne)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROL_ID", nullable = false)
    @JsonIgnoreProperties({"usuarios", "hibernateLazyInitializer", "handler"})
    private UsuarioRolEntity rol;

    // !! RELACIÓN CON GRUPO (si aplica)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GRUPO_ID")
    @JsonIgnoreProperties({"usuarios", "rol", "permisos", "hibernateLazyInitializer", "handler"})
    private UsuarioGruposEntity grupo;
}
```

### **UsuarioRolEntity.java**
```java
package com.github.maximovj.rhhub_app.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TBL_USUARIO_ROLES")
public class UsuarioRolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROL_ID")
    private Long rolId;

    @Column(name = "ROL_NOMBRE", nullable = false, unique = true)
    private String rolNombre;

    @Column(name = "ROL_DESCRIPCION", nullable = false)
    private String rolDescripcion;

    @Column(name = "ROL_ES_ADMINISTRADOR", nullable = false)
    @Builder.Default
    private Boolean rolEsAdministrador = false;

    @Column(name = "ES_ACTIVO", nullable = false)
    @Builder.Default
    private Boolean esActivo = true;

    // !! RELACIÓN INVERSA CON USUARIOS (OneToMany)
    @OneToMany(mappedBy = "rol", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"rol", "grupo", "hibernateLazyInitializer", "handler"})
    @Builder.Default
    private List<UsuarioEntity> usuarios = new ArrayList<>();

    // !! RELACIÓN CON GRUPOS (si un rol puede tener varios grupos)
    @OneToMany(mappedBy = "rol", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"rol", "usuarios", "permisos", "hibernateLazyInitializer", "handler"})
    @Builder.Default
    private List<UsuarioGruposEntity> grupos = new ArrayList<>();

    // === MÉTODOS HELPER ===
    
    public void addUsuario(UsuarioEntity usuario) {
        usuarios.add(usuario);
        usuario.setRol(this);
    }
    
    public void removeUsuario(UsuarioEntity usuario) {
        usuarios.remove(usuario);
        usuario.setRol(null);
    }
    
    public void addGrupo(UsuarioGruposEntity grupo) {
        grupos.add(grupo);
        grupo.setRol(this);
    }
    
    // Método para contar usuarios activos con este rol
    public long contarUsuariosActivos() {
        return usuarios.stream()
            .filter(UsuarioEntity::getEsActivo)
            .count();
    }
    
    // Método estático para builder
    public static UsuarioRolEntityBuilder builder() {
        return new UsuarioRolEntityBuilder()
            .rolEsAdministrador(false)
            .esActivo(true)
            .usuarios(new ArrayList<>())
            .grupos(new ArrayList<>());
    }
}
```

## **Ventajas de esta arquitectura:**

### **Consulta de usuarios por rol:**
```java
@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    
    // Fácil y eficiente
    List<UsuarioEntity> findByRol_RolId(Long rolId);
    List<UsuarioEntity> findByRol_RolNombre(String rolNombre);
    
    // Usuarios activos por rol
    List<UsuarioEntity> findByRol_RolIdAndEsActivoTrue(Long rolId);
}
```

### **Consulta de roles con estadísticas:**
```java
@Repository
public interface UsuarioRolRepository extends JpaRepository<UsuarioRolEntity, Long> {
    
    // Roles con conteo de usuarios
    @Query("SELECT r.rolNombre, COUNT(u) FROM UsuarioRolEntity r " +
           "LEFT JOIN r.usuarios u GROUP BY r.rolId")
    List<Object[]> contarUsuariosPorRol();
}
```

## **Reglas de decisión:**

### **Usa `ManyToOne` (Opción 1) si:**
- Cada usuario tiene **exactamente un rol**
- Sistema simple de permisos
- Rendimiento crítico
- Menos complejidad

### **Usa `ManyToMany` (Opción 2) si:**
- Usuarios pueden tener **múltiples roles** simultáneamente
- Sistema complejo de autorización
- Flexibilidad para cambios futuros

### **Usa tabla intermedia (Opción 3) si:**
- Necesitas **auditoría/historial** de asignaciones
- Hay **fechas de vigencia** para roles
- **Aprobaciones** para cambio de roles
- Sistema empresarial con compliance

## **Para tu proyecto:**

**Dado que ya tienes `UsuarioGruposEntity` con permisos**, recomiendo:

```java
// UsuarioEntity se relaciona con Grupo (que a su vez tiene Rol)
UsuarioEntity → UsuarioGruposEntity → UsuarioRolEntity

// O directamente con Rol si es más simple:
UsuarioEntity → UsuarioRolEntity
```

**Mi recomendación final:**
Si tu sistema es **usuario → grupo → permisos**, entonces:
- **Usuario → Grupo** (ManyToOne)
- **Grupo → Rol** (ManyToOne)
- **Rol** define el tipo (ADMIN/USUARIO)

Si es más simple **usuario → rol directo**, entonces:
- **Usuario → Rol** (ManyToOne) - **¡Esta es la más simple y recomendada!**