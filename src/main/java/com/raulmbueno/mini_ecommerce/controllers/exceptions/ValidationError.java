package com.raulmbueno.mini_ecommerce.controllers.exceptions;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ValidationError extends StandardError {

    private static final long serialVersionUID = 1L;
    private List<FieldMessage> errors = new ArrayList<>();

    public ValidationError(Instant timestamp, Integer status, String error, String message, String path) {
        super(timestamp, status, error, message, path); 
    }
    public void addError(String fieldName, String message) {
        errors.add(new FieldMessage(fieldName, message));
    }
}