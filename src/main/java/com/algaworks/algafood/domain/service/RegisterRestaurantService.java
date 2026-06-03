package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.RestaurantNotFoundException;
import com.algaworks.algafood.domain.model.City;
import com.algaworks.algafood.domain.model.Kitchen;
import com.algaworks.algafood.domain.model.PaymentMethod;
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

    @Autowired
    private RegisterPaymentMethodService registerPaymentMethod;

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

    @Transactional
    public void disassociatePaymentMethod(Long restaurantId, Long paymentMethodId){
        Restaurant restaurant = searchOrError(restaurantId);
        PaymentMethod paymentMethod = registerPaymentMethod.searchOrFail(paymentMethodId);

        restaurant.deletePaymentMethod(paymentMethod);
    }

    @Transactional
    public void associatePaymentMethod(Long restaurantId, Long paymentMethodId){
        Restaurant restaurant = searchOrError(restaurantId);
        PaymentMethod paymentMethod = registerPaymentMethod.searchOrFail(paymentMethodId);

        restaurant.createPaymentMethod(paymentMethod);
    }

    @Transactional
    public void open(Long restaurantId){
        Restaurant restaurantActual = searchOrError(restaurantId);

        restaurantActual.open();
    }

    public void close(Long restaurantId){
        Restaurant restaurantActual = searchOrError(restaurantId);

        restaurantActual.close();
    }

    public Restaurant searchOrError(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));
    }
}