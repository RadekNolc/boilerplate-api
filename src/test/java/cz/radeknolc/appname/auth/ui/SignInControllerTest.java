package cz.radeknolc.appname.auth.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.radeknolc.appname.auth.domain.usecase.SignInUseCase;
import cz.radeknolc.appname.auth.ui.dto.request.SignInRequest;
import cz.radeknolc.appname.auth.ui.dto.response.SignInResponse;
import cz.radeknolc.appname.shared.problem.domain.ProblemCode;
import cz.radeknolc.appname.shared.problem.domain.enumeration.ApiProblemCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Set;
import java.util.stream.Stream;

import static cz.radeknolc.appname.shared.problem.domain.enumeration.ApiProblemCode.VALIDATION_ERROR;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class SignInControllerTest {

    @MockBean
    private SignInUseCase signInUseCase;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void signIn_ValidCredentials_Success() throws Exception {
        // given
        String token = "justSomeToken";
        SignInRequest signInRequest = new SignInRequest("user", "mysecretpassword");

        SignInResponse expectedResponse = new SignInResponse(token);

        given(signInUseCase.signIn(any())).willReturn(expectedResponse);

        // when
        ResultActions response = mockMvc.perform(post("/api/authentication/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInRequest)));

        // then
        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.token").value(token));
    }

    @ParameterizedTest
    @MethodSource("provideDataForHandlingExceptions")
    void signIn_HandlingExceptions_HandledException(Throwable throwable, HttpStatus httpStatus, ProblemCode problemCode) throws Exception {
        // given
        SignInRequest signInRequest = new SignInRequest("user", "mysecretpassword");

        given(signInUseCase.signIn(any())).willThrow(throwable);

        // when
        ResultActions response = mockMvc.perform(post("/api/authentication/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInRequest)));

        // then
        response.andExpect(status().is(httpStatus.value()));
        response.andExpect(jsonPath("$.message").value(String.valueOf(problemCode)));
    }

    @Test
    void signIn_InvalidInputValue_HandledConstraintViolation() throws Exception {
        // given
        SignInRequest signInRequest = new SignInRequest("", "");

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<SignInRequest>> constraintViolations = validator.validate(signInRequest);

        given(signInUseCase.signIn(any())).willThrow(new ConstraintViolationException(constraintViolations));

        // when
        ResultActions response = mockMvc.perform(post("/api/authentication/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInRequest)));

        // then
        response.andExpect(status().isUnprocessableEntity());
        response.andExpect(jsonPath("$.message").value(VALIDATION_ERROR.toString()));
        response.andExpect(jsonPath("$.errors.length()").value(2));
    }

    private static Stream<Arguments> provideDataForHandlingExceptions() {
        String exceptionMessage = "Some exception message";
        return Stream.of(
                Arguments.of(new BadCredentialsException(exceptionMessage), HttpStatus.UNAUTHORIZED, ApiProblemCode.BAD_CREDENTIALS),
                Arguments.of(new DisabledException(exceptionMessage), HttpStatus.UNAUTHORIZED, ApiProblemCode.ACCOUNT_DISABLED),
                Arguments.of(new AccountExpiredException(exceptionMessage), HttpStatus.UNAUTHORIZED, ApiProblemCode.ACCOUNT_EXPIRED),
                Arguments.of(new LockedException(exceptionMessage), HttpStatus.UNAUTHORIZED, ApiProblemCode.ACCOUNT_LOCKED),
                Arguments.of(new CredentialsExpiredException(exceptionMessage), HttpStatus.UNAUTHORIZED, ApiProblemCode.CREDENTIALS_EXPIRED)
        );
    }

}