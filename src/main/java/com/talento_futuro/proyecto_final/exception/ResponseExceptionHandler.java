package com.talento_futuro.proyecto_final.exception;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice 
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(ModelNotFoundException.class)
    public ErrorResponse handleModelNotFoundException(ModelNotFoundException ex, WebRequest request) {
        return ErrorResponse.builder(ex, HttpStatus.NOT_FOUND, ex.getMessage())
                .title("Model not Found")
                .type(URI.create(request.getContextPath()))
                .property("valor", "valor - 1")
                .build();
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ErrorResponse handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
        return ErrorResponse.builder(ex, HttpStatus.UNAUTHORIZED, ex.getMessage())
                .title("Unauthorized Access")
                .type(URI.create(request.getContextPath()))
                .property("valor", "valor - 2")  // Aquí puedes agregar cualquier propiedad adicional que desees.
                .build();
    }
}
