package com.radeknolc.apiname.shared.general.infrastructure.validation;

import jakarta.persistence.Table;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExistsValidatorTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private Exists exists;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        doReturn(TableEntity.class).when(exists).entity();
        given(exists.column()).willReturn("someFieldName");
    }

    @Test
    void isValid_NotExisting_False() {
        // given
        String fieldValue = "someUsername";
        when(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM \"%s\" WHERE \"%s\" = ?".formatted("someTableName", "someFieldName"), Integer.class, fieldValue)).thenReturn(0);

        ExistsValidator existsValidator = new ExistsValidator(jdbcTemplate);
        existsValidator.initialize(exists);

        // when
        boolean result = existsValidator.isValid(fieldValue, constraintValidatorContext);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void isValid_Existing_True() {
        // given
        String fieldValue = "someUsername";
        when(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM \"%s\" WHERE \"%s\" = ?".formatted("someTableName", "someFieldName"), Integer.class, fieldValue)).thenReturn(1);

        ExistsValidator existsValidator = new ExistsValidator(jdbcTemplate);
        existsValidator.initialize(exists);

        // when
        boolean result = existsValidator.isValid(fieldValue, constraintValidatorContext);

        // then
        assertThat(result).isTrue();
    }

    @Table(name = "someTableName")
    static class TableEntity {
    }
}