package com.vecanhac.ddd.application.exception;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldErrorResponse {

    private String field;
    private String message;
}
