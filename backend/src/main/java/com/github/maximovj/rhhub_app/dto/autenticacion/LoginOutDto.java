package com.github.maximovj.rhhub_app.dto.autenticacion;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginOutDto {

    @JsonProperty(value = "token")
    private String token;

    @JsonProperty(value = "renovar_token")
    private String renovarToken;

    @JsonProperty(value = "tipo_token")
    private String tipoToken;
    
    // Constructor personalizado adicional
    public LoginOutDto(String token, String renovarToken) {
        this.token = token;
        this.renovarToken = renovarToken;
        this.tipoToken = "Bearer";
    }
    
}