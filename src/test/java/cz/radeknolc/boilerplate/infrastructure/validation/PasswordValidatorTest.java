package cz.radeknolc.boilerplate.infrastructure.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PasswordValidatorTest {

    @Mock
    private Password password;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    private static final String initialPassword = "loremipsum";

    @BeforeEach
    void setUp() {
        given(password.minSpecials()).willReturn(0);
        given(password.minNumbers()).willReturn(0);
        given(password.minCapitals()).willReturn(0);
    }

    @ParameterizedTest
    @MethodSource("provideStringsForMinimumSpecialsTest")
    void isValid_MinimumSpecials_Validated(String input, boolean valid) {
        // given
        given(password.minSpecials()).willReturn(2);

        PasswordValidator passwordValidator = new PasswordValidator();
        passwordValidator.initialize(password);

        // when
        boolean result = passwordValidator.isValid(input, constraintValidatorContext);

        // then
        assertThat(result).isEqualTo(valid);
    }

    @ParameterizedTest
    @MethodSource("provideStringsForMinimumNumbersTest")
    void isValid_MinimumNumbers_Validated(String input, boolean valid) {
        // given
        given(password.minNumbers()).willReturn(2);

        PasswordValidator passwordValidator = new PasswordValidator();
        passwordValidator.initialize(password);

        // when
        boolean result = passwordValidator.isValid(input, constraintValidatorContext);

        // then
        assertThat(result).isEqualTo(valid);
    }

    @ParameterizedTest
    @MethodSource("provideStringsForMinimumCapitalsTest")
    void isValid_MinimumCapitals_Validated(String input, boolean valid) {
        // given
        given(password.minCapitals()).willReturn(2);

        PasswordValidator passwordValidator = new PasswordValidator();
        passwordValidator.initialize(password);

        // when
        boolean result = passwordValidator.isValid(input, constraintValidatorContext);

        // then
        assertThat(result).isEqualTo(valid);
    }

    private static Stream<Arguments> provideStringsForMinimumSpecialsTest() {
        return Stream.of(
                Arguments.of("%s".formatted(initialPassword), false),
                Arguments.of("-%s".formatted(initialPassword), false),
                Arguments.of("@%s+".formatted(initialPassword), true),
                Arguments.of("*+%s&".formatted(initialPassword), true)
        );
    }

    private static Stream<Arguments> provideStringsForMinimumNumbersTest() {
        return Stream.of(
                Arguments.of("%s".formatted(initialPassword), false),
                Arguments.of("1%s".formatted(initialPassword), false),
                Arguments.of("1%s2".formatted(initialPassword), true),
                Arguments.of("1%s23".formatted(initialPassword), true)
        );
    }

    private static Stream<Arguments> provideStringsForMinimumCapitalsTest() {
        return Stream.of(
                Arguments.of("%s".formatted(initialPassword), false),
                Arguments.of("A%s".formatted(initialPassword), false),
                Arguments.of("A%sB".formatted(initialPassword), true),
                Arguments.of("A%sBC".formatted(initialPassword), true)
        );
    }
}