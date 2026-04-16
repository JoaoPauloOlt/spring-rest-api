package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EntityInUseException;
import com.algaworks.algafood.domain.exception.KitchenNotFoundException;
import com.algaworks.algafood.domain.model.Kitchen;
import com.algaworks.algafood.domain.repository.KitchenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class RegisterKitchenService {

    public static final String MSG_KITCHEN_IN_USE = "Code kitchen %d cannot be removed as it is in use";
    @Autowired
    private KitchenRepository kitchenRepository;

    public Kitchen save(Kitchen kitchen) {
        return kitchenRepository.save(kitchen);
    }

    public void delete(Long kitchenId) {
        try {
            kitchenRepository.deleteById(kitchenId);

        } catch (EmptyResultDataAccessException e) {
            throw new KitchenNotFoundException(kitchenId);

        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(String.format(MSG_KITCHEN_IN_USE, kitchenId));
        }
    }

    public Kitchen searchOrError(Long kitchenId) {
        return kitchenRepository.findById(kitchenId)
                .orElseThrow(() -> new KitchenNotFoundException(kitchenId));
    }
}