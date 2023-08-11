package com.globits.da.commons;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@Builder
public class ApiMessageError implements ApiError, Serializable {
    private final String message;
}
