package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.exception.BusinessException;
import com.algaworks.algafood.domain.exception.EntityNotFoundException;
import com.algaworks.algafood.domain.exception.KitchenNotFoundException;
import com.algaworks.algafood.domain.model.Restaurant;
import com.algaworks.algafood.domain.repository.RestaurantRepository;
import com.algaworks.algafood.domain.service.RegisterRestaurantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RegisterRestaurantService registerRestaurant;

    @GetMapping
    public List<Restaurant> list(){
        return restaurantRepository.findAll();
    }

    @GetMapping("/{restaurantId}")
    public Restaurant search(@PathVariable Long restaurantId){
        return registerRestaurant.searchOrError(restaurantId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Restaurant add(@RequestBody @Valid Restaurant restaurant){
        try {
            return registerRestaurant.save(restaurant);
        }catch (KitchenNotFoundException e){
            throw new BusinessException(e.getMessage());
        }
    }

    @PutMapping("/{restaurantId}")
    public Restaurant update(@PathVariable Long restaurantId, @RequestBody @Valid Restaurant restaurant){
        try {
            Restaurant restaurantActual = registerRestaurant.searchOrError(restaurantId);

            BeanUtils.copyProperties(restaurant, restaurantActual, "id", "paymentMethods", "address", "dateRegister", "product");

            return registerRestaurant.save(restaurantActual);
        }catch (KitchenNotFoundException e){
            throw new BusinessException(e.getMessage());
        }
    }

    @PatchMapping("/{restaurantId}")
    public Restaurant updatePartial(@PathVariable Long restaurantId, @RequestBody Map<String, Object> fields, HttpServletRequest request){
        Restaurant restaurantActual = registerRestaurant.searchOrError(restaurantId);

        merge(fields, restaurantActual, request);

        try {
            return update(restaurantId, restaurantActual);
        }catch (EntityNotFoundException e){
            throw new BusinessException(e.getMessage());
        }
    }

    private void merge(Map<String, Object> dataOrigin, Restaurant restaurantDestination, HttpServletRequest request){

        ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Restaurant restaurantOrigin = objectMapper.convertValue(dataOrigin, Restaurant.class);

            dataOrigin.forEach((nameProperty, valueProperty) -> {
                Field field = ReflectionUtils.findField(Restaurant.class, nameProperty);
                field.setAccessible(true);

                Object newValue = ReflectionUtils.getField(field, restaurantOrigin);

                //System.out.println(nameProperty + " = " + valueProperty + " = " + newValue);

                ReflectionUtils.setField(field, restaurantDestination, newValue);
            });
        }catch (IllegalArgumentException e){
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
        }
    }
}
