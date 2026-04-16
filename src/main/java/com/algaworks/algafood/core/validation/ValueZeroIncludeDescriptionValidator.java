package com.algaworks.algafood.core.validation;

import org.springframework.beans.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.util.Objects;

public class ValueZeroIncludeDescriptionValidator implements ConstraintValidator<ValueZeroIncludeDescription, Object> {

    private String valueField;
    private String descriptionField;
    private String descriptionRequired;

    @Override
    public void initialize(ValueZeroIncludeDescription constraintAnnotation) {
        this.valueField = constraintAnnotation.valueField();
        this.descriptionField = constraintAnnotation.descriptionField();
        this.descriptionRequired = constraintAnnotation.descriptionRequired();
    }

    @Override
    public boolean isValid(Object objectValidate, ConstraintValidatorContext context) {
        boolean valid = true;

        try {
            BigDecimal value = (BigDecimal) Objects.requireNonNull(BeanUtils.getPropertyDescriptor(objectValidate.getClass(), valueField))
                    .getReadMethod().invoke(objectValidate);

            String description = (String) Objects.requireNonNull(BeanUtils.getPropertyDescriptor(objectValidate.getClass(), descriptionField))
                    .getReadMethod().invoke(objectValidate);

            if (value != null && BigDecimal.ZERO.compareTo(value) == 0 && description != null){
                valid = description.toLowerCase().contains(this.descriptionRequired.toLowerCase());
            }
            return valid;
        }catch (Exception e){
            throw new ValidationException(e);
        }
    }
}
