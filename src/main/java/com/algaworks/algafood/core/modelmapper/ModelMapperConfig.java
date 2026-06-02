package com.algaworks.algafood.core.modelmapper;

import com.algaworks.algafood.api.model.AddressModel;
import com.algaworks.algafood.domain.model.Address;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(){
        var modelMapper = new ModelMapper();

        var addressToAddressModelTypeMap = modelMapper.createTypeMap(Address.class, AddressModel.class);

        addressToAddressModelTypeMap.<String>addMapping(addressSrc -> addressSrc.getCity().getState().getName(),
                (addressModelDest, value) -> addressModelDest.getCity().setState(value));

        return modelMapper;
    }
}
