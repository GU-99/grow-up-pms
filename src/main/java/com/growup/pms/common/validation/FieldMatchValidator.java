package com.growup.pms.common.validation;

import com.growup.pms.common.validation.constraint.FieldMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessorFactory;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.firstFieldName = constraintAnnotation.first();
        this.secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(o);

        try {
            Object firstValue = wrapper.getPropertyValue(firstFieldName);
            Object secondValue = wrapper.getPropertyValue(secondFieldName);
            return Objects.equals(firstValue, secondValue);
        } catch (BeansException e) {
            return false;
        }
    }
}
