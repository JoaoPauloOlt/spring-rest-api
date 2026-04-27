package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.model.input.CityInput;
import com.algaworks.algafood.domain.model.City;
import com.algaworks.algafood.domain.model.State;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CityInputDisassembler {

    @Autowired
    private ModelMapper modelMapper;

    public City toDomainObject(CityInput cityInput){
        return modelMapper.map(cityInput, City.class);
    }

    public void copyToDomainObject(CityInput cityInput, City city){
        //Para evitar identifier of an instance of State was altered from 1 to 2
        city.setState(new State());

        modelMapper.map(cityInput, city);
    }
}
