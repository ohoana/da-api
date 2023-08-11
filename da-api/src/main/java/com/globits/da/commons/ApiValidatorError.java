package com.globits.da.commons;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class ApiValidatorError implements ApiError, Serializable {
    private String field;
    private Object data;
    private String message;
}
