package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.RestaurantNotFoundException;
import com.algaworks.algafood.domain.model.City;
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

    @Autowired
    private RegisterCityService registerCity;

    @Transactional
    public Restaurant save(Restaurant restaurant) {
        Long kitchenId = restaurant.getKitchen().getId();
        Long cityId = restaurant.getAddress().getCity().getId();

        Kitchen kitchen = registerKitchen.searchOrError(kitchenId);
        City city = registerCity.searchOrError(cityId);

        restaurant.setKitchen(kitchen);
        restaurant.getAddress().setCity(city);

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