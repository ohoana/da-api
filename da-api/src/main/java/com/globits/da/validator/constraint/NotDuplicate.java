package com.globits.da.validator.constraint;

import com.globits.da.validator.validator.NotDuplicateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotDuplicateValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotDuplicate {
    String message() default "Code must not be duplicate";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
