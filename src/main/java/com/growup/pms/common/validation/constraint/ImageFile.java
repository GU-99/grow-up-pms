package com.growup.pms.common.validation.constraint;

import static java.lang.annotation.ElementType.FIELD;

import com.growup.pms.common.validation.ImageFileValidator;
import jakarta.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageFileValidator.class)
public @interface ImageFile {
    String message() default "{com.growup.pms.common.validation.constraint.ImageFile.message}";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
