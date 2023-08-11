package com.globits.da.exception;

import com.globits.da.commons.ApiError;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DataNotFoundException extends RuntimeException {
    private final ApiError apiError;
}
