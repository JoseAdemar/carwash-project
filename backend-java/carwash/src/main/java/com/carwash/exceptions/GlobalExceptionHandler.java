package com.carwash.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
@SuppressWarnings({"checkstyle:MissingJavadocType", "checkstyle:MissingJavadocMethod"})
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<String> argumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
    String message = "Parâmetro inválido: " + ex.getName() + " deve ser do tipo "
            +
            ex.getRequiredType().getSimpleName();
    return ResponseEntity.badRequest().body(message);
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex, HttpServletRequest request) {
    ErrorResponse errorResponse = new ErrorResponse(ex.getReason(), request.getRequestURI());
    return new ResponseEntity<>(errorResponse, ex.getStatusCode());
  }
}

