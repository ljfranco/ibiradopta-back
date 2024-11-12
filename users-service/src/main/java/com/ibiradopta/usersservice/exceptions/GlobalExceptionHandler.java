package com.ibiradopta.usersservice.exceptions;

import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotAuthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    // Maneja la excepción ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Maneja la excepción NotAuthorizedException (no autorizado)
    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleNotAuthorizedException(AuthorizationDeniedException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse("Unauthorized access: " + ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    // Maneja otras excepciones generales
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleGlobalException(Exception ex, WebRequest request) {
        return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
