package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.RestaurantInputDisassembler;
import com.algaworks.algafood.api.assembler.RestaurantModelAssembler;
import com.algaworks.algafood.api.model.RestaurantModel;
import com.algaworks.algafood.api.model.input.RestaurantInput;
import com.algaworks.algafood.domain.exception.BusinessException;
import com.algaworks.algafood.domain.exception.CityNotFoundException;
import com.algaworks.algafood.domain.exception.KitchenNotFoundException;
import com.algaworks.algafood.domain.model.Restaurant;
import com.algaworks.algafood.domain.repository.RestaurantRepository;
import com.algaworks.algafood.domain.service.RegisterRestaurantService;
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
        }catch (KitchenNotFoundException | CityNotFoundException e){
            throw new BusinessException(e.getMessage());
        }
    }

    @PutMapping("/{restaurantId}")
    public RestaurantModel update(@PathVariable Long restaurantId, @RequestBody @Valid RestaurantInput restaurantInput){
        try {
            Restaurant restaurantActual = registerRestaurant.searchOrError(restaurantId);

            restaurantInputDisassembler.copyToDomainObject(restaurantInput, restaurantActual);

            return restaurantModelAssembler.toModel(registerRestaurant.save(restaurantActual));
        }catch (KitchenNotFoundException | CityNotFoundException e){
            throw new BusinessException(e.getMessage());
        }
    }

    @PutMapping("/{restaurantId}/active")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activate(@PathVariable Long restaurantId){
        registerRestaurant.activate(restaurantId);
    }

    @PutMapping("/{restaurantId}/open")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void open(@PathVariable Long restaurantId){
        registerRestaurant.open(restaurantId);
    }

    @PutMapping("/{restaurantId}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void close(@PathVariable Long restaurantId){
        registerRestaurant.close(restaurantId);
    }

    @DeleteMapping("/{restaurantId}/active")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disable(@PathVariable Long restaurantId){
        registerRestaurant.disable(restaurantId);
    }
}
