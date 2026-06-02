package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.RestaurantNotFoundException;
import com.algaworks.algafood.domain.model.Kitchen;
import com.algaworks.algafood.domain.model.Restaurant;
import com.algaworks.algafood.domain.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterRestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RegisterKitchenService registerKitchen;

    @Transactional
    public Restaurant save(Restaurant restaurant) {
        Long kitchenId = restaurant.getKitchen().getId();

        Kitchen kitchen = registerKitchen.searchOrError(kitchenId);
        restaurant.setKitchen(kitchen);
        return restaurantRepository.save(restaurant);
    }

    @Transactional
    public void activate(Long restaurantId){
        Restaurant restaurantActual = searchOrError(restaurantId);

        restaurantActual.activate();
    }

    @Transactional
    public void disable(Long restaurantId){
        Restaurant restaurantActual = searchOrError(restaurantId);

        restaurantActual.disable();
    }

    public Restaurant searchOrError(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));
    }
}