package com.algaworks.algafood.domain.exception;

public class GrupoNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public GrupoNotFoundException(String message) {
        super(message);
    }

    public GrupoNotFoundException(Long stateId){
        this(String.format("There is no group registration with code %d", stateId));
    }
}
