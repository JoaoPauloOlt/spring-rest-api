package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EntityInUseException;
import com.algaworks.algafood.domain.exception.PaymentMethodNotFoundException;
import com.algaworks.algafood.domain.model.PaymentMethod;
import com.algaworks.algafood.domain.repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterPaymentMethodService {

    private static final String MSG_PAYMENT_METHOD_IN_USE = "Payment method code %d cannot be removed as it is in use";

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Transactional
    public PaymentMethod save(PaymentMethod paymentMethod){
        return paymentMethodRepository.save(paymentMethod);
    }

    @Transactional
    public void delete(Long paymentMethodId){
        try {
            paymentMethodRepository.deleteById(paymentMethodId);
            paymentMethodRepository.flush();
        } catch (EmptyResultDataAccessException e){
            throw new PaymentMethodNotFoundException(paymentMethodId);
        } catch (DataIntegrityViolationException e){
            throw new EntityInUseException(String.format(MSG_PAYMENT_METHOD_IN_USE, paymentMethodId));
        }
    }

    public PaymentMethod searchOrFail(Long paymentMethodId){
        return paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> new PaymentMethodNotFoundException(paymentMethodId));
    }
}
