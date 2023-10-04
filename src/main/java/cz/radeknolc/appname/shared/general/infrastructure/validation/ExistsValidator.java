package cz.radeknolc.appname.shared.general.infrastructure.validation;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
public class ExistsValidator implements ConstraintValidator<Exists, CharSequence> {

    private final JdbcTemplate jdbcTemplate;

    private String table;
    private String column;

    @Override
    public void initialize(Exists constraintAnnotation) {
        try {
            table = constraintAnnotation.entity().getAnnotation(Table.class).name();
        } catch (NullPointerException exception) {
            table = constraintAnnotation.entity().getAnnotation(Entity.class).name();
        }
        column = constraintAnnotation.column();
    }

    @Override
    public boolean isValid(CharSequence charSequence, ConstraintValidatorContext constraintValidatorContext) {
        Integer result = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM \"%s\" WHERE \"%s\" = ?".formatted(table, column), Integer.class, charSequence.toString());
        return result != null && result == 1;
    }
}
