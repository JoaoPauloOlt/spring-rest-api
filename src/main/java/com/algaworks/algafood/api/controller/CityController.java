package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.domain.exception.BusinessException;
import com.algaworks.algafood.domain.exception.EntityNotFoundException;
import com.algaworks.algafood.domain.exception.StateNotFoundException;
import com.algaworks.algafood.domain.model.City;
import com.algaworks.algafood.domain.repository.CityRepository;
import com.algaworks.algafood.domain.service.RegisterCityService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/cities")
public class CityController {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private RegisterCityService registerCity;

    @GetMapping
    public List<City> list(){
        return cityRepository.findAll();
    }

    @GetMapping("/{cityId}")
    public City search(@PathVariable Long cityId){
        return registerCity.searchOrError(cityId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public City add(@RequestBody @Valid City city){
        try {
            return registerCity.save(city);
        }catch (StateNotFoundException e){
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @PutMapping("/{cityId}")
    public City update(@PathVariable Long cityId, @RequestBody @Valid City city){
        City cityActual = registerCity.searchOrError(cityId);

        BeanUtils.copyProperties(city, cityActual, "id");

        try {
            return registerCity.save(cityActual);
        }catch (StateNotFoundException e){
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/{cityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long cityId){
        registerCity.delete(cityId);
    }
}