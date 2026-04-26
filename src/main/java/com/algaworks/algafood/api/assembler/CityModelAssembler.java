package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.model.CityModel;
import com.algaworks.algafood.domain.model.City;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CityModelAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public CityModel toModel(City city){
        return modelMapper.map(city, CityModel.class);
    }

    public List<CityModel> toCollectionModel(List<City> cities){
        return cities.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}
