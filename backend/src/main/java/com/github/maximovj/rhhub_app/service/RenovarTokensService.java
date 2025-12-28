package com.github.maximovj.rhhub_app.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.maximovj.rhhub_app.entity.RenovarTokensEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioEntity;
import com.github.maximovj.rhhub_app.repository.RenovarTokensRepository;

import jakarta.transaction.Transactional;

@Service
public class RenovarTokensService {

    private RenovarTokensRepository refreshTokenRepository;

    private final long refreshExpirationMs = 7 * 24 * 60 * 60 * 1000; // 7 días

    @Autowired
    public RenovarTokensService(RenovarTokensRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RenovarTokensEntity createRefreshToken(UsuarioEntity usuario) {
        RenovarTokensEntity refreshToken = RenovarTokensEntity.builder()
                .usuario(usuario)
                .token(UUID.randomUUID().toString())
                .fechaDeExpiracion(Instant.now().plusMillis(refreshExpirationMs))
                .suspendido(false)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public boolean isValid(RenovarTokensEntity token) {
        return !token.estaSuspendido() && token.getFechaDeExpiracion().isAfter(Instant.now());
    }

    public void revoke(RenovarTokensEntity token) {
        token.suspendido(true);
        refreshTokenRepository.save(token);
    }

    @Transactional
    public void revokeAllTokens(UsuarioEntity usuario) {
        refreshTokenRepository.deleteAllByUsuario(usuario);
    }
    
}
