package com.radeknolc.apiname.shared.general.infrastructure.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, CharSequence> {

    private int minCapitals;
    private int minNumbers;
    private int minSpecials;
    private int minSize;

    @Override
    public void initialize(Password constraintAnnotation) {
        this.minCapitals = constraintAnnotation.minCapitals();
        this.minNumbers = constraintAnnotation.minNumbers();
        this.minSpecials = constraintAnnotation.minSpecials();
        this.minSize = constraintAnnotation.minSize();
    }

    @Override
    public boolean isValid(CharSequence charSequence, ConstraintValidatorContext constraintValidatorContext) {
        String regex = "^(?=(.*[-\\#\\$\\.\\%\\&\\*\\+\\@]){" + minSpecials + ",})(?=(.*\\d){" + minNumbers + ",})(?=(.*[A-Z]){" + minCapitals + ",}).*$";
        if (charSequence == null) {
            return false;
        }

        String password = charSequence.toString();
        return password.length() >= minSize && password.matches(regex);
    }
}
