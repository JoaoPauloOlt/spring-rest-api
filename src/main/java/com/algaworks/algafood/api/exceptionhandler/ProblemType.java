package com.algaworks.algafood.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {

    MESSAGE_INCOMPREHENSIBLE("message-incomprehensible", "Message incomprehensible"),
    RESOURCE_NOT_FOUND("/resource-not-found", "Resource not found"),
    INVALID_DATA("/invalid-data", "Invalid data"),
    SYSTEM_ERROR("/system-error", "System error"),
    ENTITY_IN_USE("/entity-in-use", "Entity in use"),
    ERROR_BUSINESS("/error-business", "Business rule violation"),
    PARAMETER_INVALID("parameter-invalid", "Parameter invalid");

    private String title;
    private String uri;

    ProblemType(String path, String title) {
        this.uri = "https://algafood.com.br" + path;
        this.title = title;
    }
}
