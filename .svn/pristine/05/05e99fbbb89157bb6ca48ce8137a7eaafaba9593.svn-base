package com.globits.da.validator.constraint;

import com.globits.da.validator.validator.NotContainSpaceValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotContainSpaceValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotContainSpace {
    String message() default "Code must not contain space and length must be between 6 and 10";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
