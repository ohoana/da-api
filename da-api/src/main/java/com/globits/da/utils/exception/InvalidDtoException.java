package com.globits.da.utils.exception;

import java.util.Map;

public class InvalidDtoException extends RuntimeException{
    private final Map<String, String> errors;

    public InvalidDtoException(Map<String, String> errors) {
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
