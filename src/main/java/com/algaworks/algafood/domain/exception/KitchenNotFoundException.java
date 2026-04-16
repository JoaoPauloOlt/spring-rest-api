package com.algaworks.algafood.domain.exception;

public class KitchenNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public KitchenNotFoundException(String message) {
        super(message);
    }

    public KitchenNotFoundException(Long kitchenId) {
        this(String.format("there is no kitchen with code %d", kitchenId));
    }
}
