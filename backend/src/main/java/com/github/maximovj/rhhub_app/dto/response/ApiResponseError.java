package com.github.maximovj.rhhub_app.dto.response;

import java.util.List;

import org.springframework.http.HttpStatus;

public class ApiResponseError <T> extends ApiResponseDetailsDto<T> {

    public ApiResponseError(
        HttpStatus status,
        String messageError,
        List<ApiErrorDto> errors
    ) {
        super(
            status.value(),
            status.getReasonPhrase(),
            "error",
            false,
            null,
            null,
            messageError,
            errors,
            null);
    }
    
}
