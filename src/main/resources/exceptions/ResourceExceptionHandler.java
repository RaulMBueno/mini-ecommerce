package com.raulmbueno.mini_ecommerce.resources.exceptions; 
import com.raulmbueno.mini_ecommerce.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        
        HttpStatus status = HttpStatus.NOT_FOUND;
        
        StandardError err = new StandardError(
            Instant.now(),
            status.value(), 
            "Resource Not Found",
            e.getMessage(), 
            request.getRequestURI() 
        );
        
        return ResponseEntity.status(status).body(err);
    }
}