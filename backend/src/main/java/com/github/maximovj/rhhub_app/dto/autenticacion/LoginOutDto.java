package com.github.maximovj.rhhub_app.dto.autenticacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginOutDto {

    private String token;
    private String tipoToken;
    
    // Constructor personalizado adicional
    public LoginOutDto(String token) {
        this.token = token;
        this.tipoToken = "Bearer";
    }
    
}