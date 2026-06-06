package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.RestaurantNotFoundException;
import com.algaworks.algafood.domain.model.*;
import com.algaworks.algafood.domain.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Autowired
    private RegisterUserService registerUserService;

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
    public void activate(List<Long> restaurantIds){
        restaurantIds.forEach(this::activate);
    }

    @Transactional
    public void disable(List<Long> restaurantIds){
        restaurantIds.forEach(this::disable);
    }

    @Transactional
    public void disassociatePaymentMethod(Long restaurantId, Long paymentMethodId){
        Restaurant restaurant = searchOrError(restaurantId);
        PaymentMethod paymentMethod = registerPaymentMethod.searchOrFail(paymentMethodId);

        restaurant.removePaymentMethod(paymentMethod);
    }

    @Transactional
    public void associatePaymentMethod(Long restaurantId, Long paymentMethodId){
        Restaurant restaurant = searchOrError(restaurantId);
        PaymentMethod paymentMethod = registerPaymentMethod.searchOrFail(paymentMethodId);

        restaurant.addPaymentMethod(paymentMethod);
    }

    @Transactional
    public void open(Long restaurantId){
        Restaurant restaurantActual = searchOrError(restaurantId);

        restaurantActual.open();
    }

    @Transactional
    public void close(Long restaurantId){
        Restaurant restaurantActual = searchOrError(restaurantId);

        restaurantActual.close();
    }

    @Transactional
    public void disassociate(Long restaurantId, Long userId){
        Restaurant restaurant = searchOrError(restaurantId);
        User user = registerUserService.searchOrFail(userId);

        restaurant.removeResponsible(user);
    }

    @Transactional
    public void associate(Long restaurantId, Long userId){
        Restaurant restaurant = searchOrError(restaurantId);
        User user = registerUserService.searchOrFail(userId);

        restaurant.addResponsible(user);
    }

    public Restaurant searchOrError(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));
    }
}