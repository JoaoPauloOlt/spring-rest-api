package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.CityInputDisassembler;
import com.algaworks.algafood.api.assembler.CityModelAssembler;
import com.algaworks.algafood.api.model.CityModel;
import com.algaworks.algafood.api.model.input.CityInput;
import com.algaworks.algafood.domain.exception.BusinessException;
import com.algaworks.algafood.domain.exception.StateNotFoundException;
import com.algaworks.algafood.domain.model.City;
import com.algaworks.algafood.domain.repository.CityRepository;
import com.algaworks.algafood.domain.service.RegisterCityService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cities")
public class CityController {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private RegisterCityService registerCity;

    @Autowired
    private CityModelAssembler cityModelAssembler;

    @Autowired
    private CityInputDisassembler cityInputDisassembler;

    @GetMapping
    public List<CityModel> list(){
        List<City> allCities = cityRepository.findAll();

        return cityModelAssembler.toCollectionModel(allCities);
    }

    @GetMapping("/{cityId}")
    public CityModel search(@PathVariable Long cityId){
        City city = registerCity.searchOrError(cityId);

        return cityModelAssembler.toModel(city);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CityModel add(@RequestBody @Valid CityInput cityInput){
        try {
            City city = cityInputDisassembler.toDomainObject(cityInput);
            city = registerCity.save(city);

            return cityModelAssembler.toModel(city);
        }catch (StateNotFoundException e){
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @PutMapping("/{cityId}")
    public CityModel update(@PathVariable Long cityId, @RequestBody @Valid CityInput cityInput){
        try {
            City cityActual = registerCity.searchOrError(cityId);

            cityInputDisassembler.copyToDomainObject(cityInput, cityActual);
            cityActual = registerCity.save(cityActual);

            return cityModelAssembler.toModel(cityActual);
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