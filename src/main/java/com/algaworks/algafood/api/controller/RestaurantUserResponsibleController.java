package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.UserModelAssembler;
import com.algaworks.algafood.api.model.UserModel;
import com.algaworks.algafood.domain.model.Restaurant;
import com.algaworks.algafood.domain.service.RegisterRestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/restaurants/{restaurantId}/responsibles")
public class RestaurantUserResponsibleController {

    @Autowired
    private RegisterRestaurantService registerRestaurant;

    @Autowired
    private UserModelAssembler userModelAssembler;

    @GetMapping
    public List<UserModel> list(@PathVariable Long restaurantId){
        Restaurant restaurant = registerRestaurant.searchOrError(restaurantId);

        return userModelAssembler.toCollectionModel(restaurant.getResponsible());
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void associate(@PathVariable Long restaurantId, @PathVariable Long userId) {
        registerRestaurant.associate(restaurantId, userId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disassociate(@PathVariable Long restaurantId, @PathVariable Long userId) {
        registerRestaurant.disassociate(restaurantId, userId);
    }
}