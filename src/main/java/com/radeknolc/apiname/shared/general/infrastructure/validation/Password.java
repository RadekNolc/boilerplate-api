package com.radeknolc.apiname.shared.general.infrastructure.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface Password {
    String message() default "PASSWORD";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int minSize() default 8;
    int minCapitals() default 0;
    int minNumbers() default 0;
    int minSpecials() default 0;
}
