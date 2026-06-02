package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.model.input.PaymentMethodInput;
import com.algaworks.algafood.domain.model.PaymentMethod;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodInputDisassembler {

    @Autowired
    private ModelMapper modelMapper;

    public PaymentMethod toDomainObject(PaymentMethodInput paymentMethodInput){
        return modelMapper.map(paymentMethodInput, PaymentMethod.class);
    }

    public void copyToDomainObject(PaymentMethodInput paymentMethodInput, PaymentMethod paymentMethod){
        modelMapper.map(paymentMethodInput, paymentMethod);
    }
}
