package com.globits.da.exception;

import com.globits.da.commons.ApiError;
import lombok.Getter;

@Getter
public class InvalidInputException extends Exception{
    private final ApiError apiError;
    public InvalidInputException(ApiError apiError) {
        this.apiError = apiError;
    }
}
