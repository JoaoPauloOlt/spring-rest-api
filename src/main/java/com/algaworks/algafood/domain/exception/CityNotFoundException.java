package com.algaworks.algafood.domain.exception;

public class CityNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public CityNotFoundException(String message) {
        super(message);
    }

    public CityNotFoundException(Long cityId) {
        this(String.format("there is no city with code %d", cityId));
    }
}
