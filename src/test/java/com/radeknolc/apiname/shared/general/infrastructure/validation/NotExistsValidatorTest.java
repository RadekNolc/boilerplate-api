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
class NotExistsValidatorTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private NotExists notExists;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        doReturn(TableEntity.class).when(notExists).entity();
        given(notExists.column()).willReturn("someFieldName");
    }

    @Test
    void isValid_NotExisting_True() {
        // given
        String fieldValue = "someUsername";
        when(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM \"%s\" WHERE \"%s\" = ?".formatted("someTableName", "someFieldName"), Integer.class, fieldValue)).thenReturn(0);

        NotExistsValidator notExistsValidator = new NotExistsValidator(jdbcTemplate);
        notExistsValidator.initialize(notExists);

        // when
        boolean result = notExistsValidator.isValid(fieldValue, constraintValidatorContext);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void isValid_Existing_False() {
        // given
        String fieldValue = "someUsername";
        when(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM \"%s\" WHERE \"%s\" = ?".formatted("someTableName", "someFieldName"), Integer.class, fieldValue)).thenReturn(1);

        NotExistsValidator notExistsValidator = new NotExistsValidator(jdbcTemplate);
        notExistsValidator.initialize(notExists);

        // when
        boolean result = notExistsValidator.isValid(fieldValue, constraintValidatorContext);

        // then
        assertThat(result).isFalse();
    }

    @Table(name = "someTableName")
    static class TableEntity {
    }
}