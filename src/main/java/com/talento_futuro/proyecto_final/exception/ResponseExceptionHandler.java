package com.talento_futuro.proyecto_final.exception;

import java.net.URI;
import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    // Validación de campos (@Valid fallida)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<String> errores = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        ErrorResponse errorResponse = ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, "Error de validación")
                .title("Validación fallida")
                .type(URI.create(request.getDescription(false)))
                .property("errores", errores)
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    // Modelo no encontrado
    @ExceptionHandler(ModelNotFoundException.class)
    public ErrorResponse handleModelNotFoundException(ModelNotFoundException ex, WebRequest request) {
        return ErrorResponse.builder(ex, HttpStatus.NOT_FOUND, ex.getMessage())
                .title("Recurso no encontrado")
                .type(URI.create(request.getDescription(false)))
                .property("codigo_error", "modelo_no_encontrado")
                .build();
    }

    // Acceso no autorizado
    @ExceptionHandler(UnauthorizedException.class)
    public ErrorResponse handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
        return ErrorResponse.builder(ex, HttpStatus.UNAUTHORIZED, ex.getMessage())
                .title("Acceso no autorizado")
                .type(URI.create(request.getDescription(false)))
                .property("codigo_error", "acceso_denegado")
                .build();
    }

    // Excepción general (por si algo se escapa)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder(ex, HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado")
                .title("Error del servidor")
                .type(URI.create(request.getDescription(false)))
                .property("detalle", ex.getMessage())
                .build();

        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
