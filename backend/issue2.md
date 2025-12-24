# Generación de Token JWT en Spring Boot 3 + Java 17

Te mostraré cómo implementar JWT en español paso a paso:

## 1. Dependencias Maven (`pom.xml`)

```xml
<dependencies>
    <!-- Spring Boot Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- Spring Security -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    
    <!-- JWT (jjwt) -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.12.3</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.12.3</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.12.3</version>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Validación -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
</dependencies>
```

## 2. Configuración de JWT (`application.yml`)

```yaml
app:
  jwt:
    clave-secreta: "miClaveSecretaSuperSeguraQueDebeSerMuyLargaParaHS512123456"
    tiempo-expiracion: 86400000 # 24 horas en milisegundos
```

## 3. Clase para Propiedades JWT

```java
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.jwt")
public class PropiedadesJwt {
    private String claveSecreta;
    private long tiempoExpiracion;

    // Getters y setters
    public String getClaveSecreta() {
        return claveSecreta;
    }

    public void setClaveSecreta(String claveSecreta) {
        this.claveSecreta = claveSecreta;
    }

    public long getTiempoExpiracion() {
        return tiempoExpiracion;
    }

    public void setTiempoExpiracion(long tiempoExpiracion) {
        this.tiempoExpiracion = tiempoExpiracion;
    }
}
```

## 4. Servicio JWT

```java
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class ServicioJwt {

    private final PropiedadesJwt propiedadesJwt;

    public ServicioJwt(PropiedadesJwt propiedadesJwt) {
        this.propiedadesJwt = propiedadesJwt;
    }

    private SecretKey obtenerClaveSecreta() {
        byte[] claveBytes = Decoders.BASE64.decode(propiedadesJwt.getClaveSecreta());
        return Keys.hmacShaKeyFor(claveBytes);
    }

    public String generarToken(String nombreUsuario) {
        return generarToken(new HashMap<>(), nombreUsuario);
    }

    public String generarToken(Map<String, Object> reclamacionesExtras, String nombreUsuario) {
        return Jwts.builder()
                .claims(reclamacionesExtras)
                .subject(nombreUsuario)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + propiedadesJwt.getTiempoExpiracion()))
                .signWith(obtenerClaveSecreta())
                .compact();
    }

    public boolean esTokenValido(String token, UserDetails detallesUsuario) {
        final String nombreUsuario = extraerNombreUsuario(token);
        return (nombreUsuario.equals(detallesUsuario.getUsername())) && !esTokenExpirado(token);
    }

    private boolean esTokenExpirado(String token) {
        return extraerExpiracion(token).before(new Date());
    }

    private Date extraerExpiracion(String token) {
        return extraerReclamacion(token, Claims::getExpiration);
    }

    public String extraerNombreUsuario(String token) {
        return extraerReclamacion(token, Claims::getSubject);
    }

    public <T> T extraerReclamacion(String token, Function<Claims, T> reclamacionesResolver) {
        final Claims reclamaciones = extraerTodasLasReclamaciones(token);
        return reclamacionesResolver.apply(reclamaciones);
    }

    private Claims extraerTodasLasReclamaciones(String token) {
        return Jwts.parser()
                .verifyWith(obtenerClaveSecreta())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
```

## 5. Filtro de Autenticación JWT

```java
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class FiltroAutenticacionJwt extends OncePerRequestFilter {

    private final ServicioJwt servicioJwt;
    private final UserDetailsService servicioDetallesUsuario;

    public FiltroAutenticacionJwt(ServicioJwt servicioJwt, UserDetailsService servicioDetallesUsuario) {
        this.servicioJwt = servicioJwt;
        this.servicioDetallesUsuario = servicioDetallesUsuario;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        final String jwt = authHeader.substring(7);
        final String nombreUsuario = servicioJwt.extraerNombreUsuario(jwt);
        
        if (nombreUsuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails detallesUsuario = this.servicioDetallesUsuario.loadUserByUsername(nombreUsuario);
            
            if (servicioJwt.esTokenValido(jwt, detallesUsuario)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    detallesUsuario,
                    null,
                    detallesUsuario.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
```

## 6. Configuración de Seguridad

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ConfiguracionSeguridad {

    private final FiltroAutenticacionJwt filtroAutenticacionJwt;

    public ConfiguracionSeguridad(FiltroAutenticacionJwt filtroAutenticacionJwt) {
        this.filtroAutenticacionJwt = filtroAutenticacionJwt;
    }

    @Bean
    public SecurityFilterChain filtroSeguridad(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(proveedorAutenticacion())
                .addFilterBefore(filtroAutenticacionJwt, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder codificadorPassword() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider proveedorAutenticacion() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(servicioDetallesUsuario());
        provider.setPasswordEncoder(codificadorPassword());
        return provider;
    }

    @Bean
    public AuthenticationManager gestorAutenticacion(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService servicioDetallesUsuario() {
        UserDetails usuario = User.builder()
                .username("usuario")
                .password(codificadorPassword().encode("password"))
                .roles("USUARIO")
                .build();
        
        UserDetails admin = User.builder()
                .username("admin")
                .password(codificadorPassword().encode("admin"))
                .roles("ADMIN")
                .build();
        
        return new InMemoryUserDetailsManager(usuario, admin);
    }
}
```

## 7. DTOs para Autenticación

```java
public record SolicitudAutenticacion(
        String nombreUsuario,
        String password
) {}

public record RespuestaAutenticacion(
        String token,
        String tipoToken
) {
    public RespuestaAutenticacion(String token) {
        this(token, "Bearer");
    }
}
```

## 8. Controlador de Autenticación

```java
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class ControladorAutenticacion {

    private final AuthenticationManager gestorAutenticacion;
    private final ServicioJwt servicioJwt;
    private final UserDetailsService servicioDetallesUsuario;

    public ControladorAutenticacion(
            AuthenticationManager gestorAutenticacion,
            ServicioJwt servicioJwt,
            UserDetailsService servicioDetallesUsuario) {
        this.gestorAutenticacion = gestorAutenticacion;
        this.servicioJwt = servicioJwt;
        this.servicioDetallesUsuario = servicioDetallesUsuario;
    }

    @PostMapping("/autenticar")
    public ResponseEntity<RespuestaAutenticacion> autenticar(
            @RequestBody SolicitudAutenticacion solicitud) {
        
        gestorAutenticacion.authenticate(
            new UsernamePasswordAuthenticationToken(
                solicitud.nombreUsuario(),
                solicitud.password()
            )
        );
        
        final UserDetails detallesUsuario = servicioDetallesUsuario
                .loadUserByUsername(solicitud.nombreUsuario());
        
        final String token = servicioJwt.generarToken(detallesUsuario.getUsername());
        
        return ResponseEntity.ok(new RespuestaAutenticacion(token));
    }

    @PostMapping("/validar")
    public ResponseEntity<Boolean> validarToken(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            try {
                String nombreUsuario = servicioJwt.extraerNombreUsuario(jwt);
                UserDetails detallesUsuario = servicioDetallesUsuario.loadUserByUsername(nombreUsuario);
                return ResponseEntity.ok(servicioJwt.esTokenValido(jwt, detallesUsuario));
            } catch (Exception e) {
                return ResponseEntity.ok(false);
            }
        }
        return ResponseEntity.ok(false);
    }
}
```

## 9. Controlador de Ejemplo Protegido

```java
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ejemplo")
public class ControladorEjemplo {

    @GetMapping("/saludo")
    public String saludo() {
        return "¡Hola! Este es un endpoint protegido con JWT";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String soloAdmin() {
        return "Este endpoint solo es accesible para administradores";
    }

    @GetMapping("/usuario")
    @PreAuthorize("hasRole('USUARIO')")
    public String soloUsuario() {
        return "Este endpoint es accesible para usuarios";
    }
}
```

## 10. Clase Principal de la Aplicación

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class AplicacionJwt {
    public static void main(String[] args) {
        SpringApplication.run(AplicacionJwt.class, args);
    }
}
```

## Pasos para Probar la Implementación:

1. **Iniciar la aplicación**
2. **Autenticarse para obtener token:**
   ```
   POST http://localhost:8080/api/auth/autenticar
   Content-Type: application/json
   
   {
     "nombreUsuario": "usuario",
     "password": "password"
   }
   ```

3. **Usar el token en requests protegidos:**
   ```
   GET http://localhost:8080/api/ejemplo/saludo
   Authorization: Bearer [tu_token_aqui]
   ```

4. **Validar token:**
   ```
   POST http://localhost:8080/api/auth/validar
   Authorization: Bearer [tu_token_aqui]
   ```

## Notas Importantes:

1. **Seguridad de la clave secreta**: Nunca codifiques la clave secreta en el código fuente. Usa variables de entorno o un sistema de gestión de secretos.

2. **Personalización**: Adapta los roles y permisos según tus necesidades.

3. **Base de datos**: Esta implementación usa usuarios en memoria. Para producción, implementa un `UserDetailsService` que consulte desde una base de datos.

4. **Renovación de tokens**: Considera implementar refresh tokens para mejor experiencia de usuario.

5. **Logout**: Para implementar logout, puedes mantener una lista negra de tokens o usar un tiempo de expiración corto.