package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.model.KitchenModel;
import com.algaworks.algafood.api.model.RestaurantModel;
import com.algaworks.algafood.domain.exception.BusinessException;
import com.algaworks.algafood.domain.exception.KitchenNotFoundException;
import com.algaworks.algafood.domain.model.Restaurant;
import com.algaworks.algafood.domain.repository.RestaurantRepository;
import com.algaworks.algafood.domain.service.RegisterRestaurantService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RegisterRestaurantService registerRestaurant;

    @GetMapping
    public List<RestaurantModel> list(){
        return toCollectionModel(restaurantRepository.findAll());
    }

    @GetMapping("/{restaurantId}")
    public RestaurantModel search(@PathVariable Long restaurantId){
        Restaurant restaurant = registerRestaurant.searchOrError(restaurantId);

        return toModel(restaurant);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantModel add(@RequestBody @Valid Restaurant restaurant){
        try {
            return toModel(registerRestaurant.save(restaurant));
        }catch (KitchenNotFoundException e){
            throw new BusinessException(e.getMessage());
        }
    }

    @PutMapping("/{restaurantId}")
    public RestaurantModel update(@PathVariable Long restaurantId, @RequestBody @Valid Restaurant restaurant){
        try {
            Restaurant restaurantActual = registerRestaurant.searchOrError(restaurantId);

            BeanUtils.copyProperties(restaurant, restaurantActual, "id", "paymentMethods", "address", "dateRegister", "product");

            return toModel(registerRestaurant.save(restaurantActual));
        }catch (KitchenNotFoundException e){
            throw new BusinessException(e.getMessage());
        }
    }

    @NonNull
    private RestaurantModel toModel(Restaurant restaurant) {
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

    private List<RestaurantModel> toCollectionModel(List<Restaurant> restaurants){
        return restaurants.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}
