package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.RestaurantInputDisassembler;
import com.algaworks.algafood.api.assembler.RestaurantModelAssembler;
import com.algaworks.algafood.api.model.RestaurantModel;
import com.algaworks.algafood.api.model.input.RestaurantInput;
import com.algaworks.algafood.domain.exception.BusinessException;
import com.algaworks.algafood.domain.exception.KitchenNotFoundException;
import com.algaworks.algafood.domain.model.Restaurant;
import com.algaworks.algafood.domain.repository.RestaurantRepository;
import com.algaworks.algafood.domain.service.RegisterRestaurantService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RegisterRestaurantService registerRestaurant;

    @Autowired
    private RestaurantModelAssembler restaurantModelAssembler;

    @Autowired
    private RestaurantInputDisassembler restaurantInputDisassembler;

    @GetMapping
    public List<RestaurantModel> list(){
        return restaurantModelAssembler.toCollectionModel(restaurantRepository.findAll());
    }

    @GetMapping("/{restaurantId}")
    public RestaurantModel search(@PathVariable Long restaurantId){
        Restaurant restaurant = registerRestaurant.searchOrError(restaurantId);

        return restaurantModelAssembler.toModel(restaurant);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantModel add(@RequestBody @Valid RestaurantInput restaurantInput){
        try {
            Restaurant restaurant = restaurantInputDisassembler.toDomainObject(restaurantInput);

            return restaurantModelAssembler.toModel(registerRestaurant.save(restaurant));
        }catch (KitchenNotFoundException e){
            throw new BusinessException(e.getMessage());
        }
    }

    @PutMapping("/{restaurantId}")
    public RestaurantModel update(@PathVariable Long restaurantId, @RequestBody @Valid RestaurantInput restaurantInput){
        try {
            Restaurant restaurant = restaurantInputDisassembler.toDomainObject(restaurantInput);
            Restaurant restaurantActual = registerRestaurant.searchOrError(restaurantId);

            BeanUtils.copyProperties(restaurant, restaurantActual, "id", "paymentMethods", "address", "dateRegister", "product");

            return restaurantModelAssembler.toModel(registerRestaurant.save(restaurantActual));
        }catch (KitchenNotFoundException e){
            throw new BusinessException(e.getMessage());
        }
    }
}
