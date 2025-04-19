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

        //@Valid erroneo 
        @Override
        protected ResponseEntity<Object> handleMethodArgumentNotValid(
                        MethodArgumentNotValidException ex,
                        HttpHeaders headers,
                        HttpStatusCode status,
                        WebRequest request) {

                List<String> errors = ex.getBindingResult()
                                .getAllErrors()
                                .stream()
                                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                .toList();

                ErrorResponse errorResponse = ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, "Validation error")
                                .title("Validation failed")
                                .type(URI.create(request.getDescription(false)))
                                .property("errors", errors)
                                .build();

                return ResponseEntity.badRequest().body(errorResponse);
        }

        // Modelo no encontrado
        @ExceptionHandler(ModelNotFoundException.class)
        public ErrorResponse handleModelNotFoundException(ModelNotFoundException ex, WebRequest request) {
                return ErrorResponse.builder(ex, HttpStatus.NOT_FOUND, ex.getMessage())
                                .title("Resource not found")
                                .type(URI.create(request.getDescription(false)))
                                .property("error_code", "model_not_found")
                                .build();
        }

        // Acceso no autorizado
        @ExceptionHandler(UnauthorizedException.class)
        public ErrorResponse handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
                return ErrorResponse.builder(ex, HttpStatus.UNAUTHORIZED, ex.getMessage())
                                .title("Unauthorized access")
                                .type(URI.create(request.getDescription(false)))
                                .property("error_code", "access_denied")
                                .build();
        }

        // Excepcion general (error no controlado)
        @ExceptionHandler(Exception.class)
        public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
                ErrorResponse errorResponse = ErrorResponse
                                .builder(ex, HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error")
                                .title("Server error")
                                .type(URI.create(request.getDescription(false)))
                                .property("detail", ex.getMessage())
                                .build();

                return ResponseEntity.internalServerError().body(errorResponse);
        }
}
