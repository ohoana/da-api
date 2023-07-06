package com.globits.da.rest;

import com.globits.da.utils.exception.InvalidDtoException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerController {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidDtoException.class)
    public Map<String, String> handlerInvalidDto(InvalidDtoException e) {
        return e.getErrors();
    }
}
