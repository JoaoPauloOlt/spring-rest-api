package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.model.KitchenModel;
import com.algaworks.algafood.api.model.RestaurantModel;
import com.algaworks.algafood.domain.model.Restaurant;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RestaurantModelAssembler {

    public RestaurantModel toModel(Restaurant restaurant) {
        KitchenModel kitchenModel = new KitchenModel();
        kitchenModel.setId(restaurant.getId());
        kitchenModel.setName(restaurant.getName());

        RestaurantModel restaurantModel = new RestaurantModel();
        restaurantModel.setId(restaurant.getId());
        restaurantModel.setName(restaurant.getName());
        restaurantModel.setShippingFee(restaurant.getShippingFee());
        restaurantModel.setKitchen(kitchenModel);
        return restaurantModel;
    }

    public List<RestaurantModel> toCollectionModel(List<Restaurant> restaurants){
        return restaurants.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}
