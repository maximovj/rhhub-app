package com.github.maximovj.rhhub_app.dto.request;

import java.time.OffsetDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FechaCreacionRequest {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @PastOrPresent(message = "La fecha desde no puede ser futura")
    @JsonProperty("desde")
    @Builder.Default
    private OffsetDateTime creado_desde = null;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @PastOrPresent(message = "La fecha hasta no puede ser futura")
    @JsonProperty("hasta")
    @Builder.Default
    private OffsetDateTime  creado_hasta = null;

    @AssertTrue(message = "La fecha desde no puede ser mayor que hasta")
    public boolean isRangoValido() {
        if (creado_desde == null || creado_hasta == null) {
            return true;
        }
        return !creado_desde.isAfter(creado_hasta);
    }

}
