package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.model.PaymentMethodModel;
import com.algaworks.algafood.domain.model.PaymentMethod;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PaymentMethodModelAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public PaymentMethodModel toModel(PaymentMethod paymentMethod){
        return modelMapper.map(paymentMethod, PaymentMethodModel.class);
    }

    public List<PaymentMethodModel> toCollectionModel(List<PaymentMethod> paymentMethods){
        return paymentMethods.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}
