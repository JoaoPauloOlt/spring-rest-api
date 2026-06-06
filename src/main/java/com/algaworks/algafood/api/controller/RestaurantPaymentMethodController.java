package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.PaymentMethodModelAssembler;
import com.algaworks.algafood.api.model.PaymentMethodModel;
import com.algaworks.algafood.domain.model.Restaurant;
import com.algaworks.algafood.domain.service.RegisterRestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/restaurants/{restaurantId}/payment-methods")
public class RestaurantPaymentMethodController {

    @Autowired
    private RegisterRestaurantService registerRestaurant;

    @Autowired
    private PaymentMethodModelAssembler paymentMethodModelAssembler;

    @GetMapping
    public List<PaymentMethodModel> list(@PathVariable Long restaurantId){
        Restaurant restaurant = registerRestaurant.searchOrError(restaurantId);

        return paymentMethodModelAssembler.toCollectionModel(restaurant.getPaymentMethods());
    }

    @PutMapping("/{paymentMethodId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void associate(@PathVariable Long restaurantId, @PathVariable Long paymentMethodId){
        registerRestaurant.associatePaymentMethod(restaurantId, paymentMethodId);
    }

    @DeleteMapping("/{paymentMethodId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disassociate(@PathVariable Long restaurantId, @PathVariable Long paymentMethodId){
        registerRestaurant.disassociatePaymentMethod(restaurantId, paymentMethodId);
    }
}
