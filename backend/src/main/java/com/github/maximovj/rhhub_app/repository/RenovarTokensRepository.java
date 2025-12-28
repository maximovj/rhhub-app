package com.github.maximovj.rhhub_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.maximovj.rhhub_app.entity.RenovarTokensEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioEntity;

public interface RenovarTokensRepository extends JpaRepository<RenovarTokensEntity, Long> {
    
    Optional<RenovarTokensEntity> findByToken(String token);

    void deleteAllByUsuario(UsuarioEntity usuario);
}
