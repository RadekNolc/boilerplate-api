package cz.radeknolc.appname.shared.general.infrastructure.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, CharSequence> {

    private String regex;

    @Override
    public void initialize(Password constraintAnnotation) {
        int minCapitals = constraintAnnotation.minCapitals();
        int minNumbers = constraintAnnotation.minNumbers();
        int minSpecials = constraintAnnotation.minSpecials();
        regex = "^(?=(.*[-\\#\\$\\.\\%\\&\\*\\+\\@]){" + minSpecials + ",})(?=(.*\\d){" + minNumbers + ",})(?=(.*[A-Z]){" + minCapitals + ",}).*$";
    }

    @Override
    public boolean isValid(CharSequence charSequence, ConstraintValidatorContext constraintValidatorContext) {
        return charSequence.toString().matches(regex);
    }
}
