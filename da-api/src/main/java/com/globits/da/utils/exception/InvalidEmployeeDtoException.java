package com.globits.da.utils.exception;

import java.util.Map;

public class InvalidEmployeeDtoException extends RuntimeException{
    private Map<String, String> errors;

    public InvalidEmployeeDtoException(Map<String, String> errors) {
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
