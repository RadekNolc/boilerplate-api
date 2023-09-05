package cz.radeknolc.appname.user.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.radeknolc.appname.shared.problem.domain.ProblemCode;
import cz.radeknolc.appname.shared.problem.domain.exception.Problem;
import cz.radeknolc.appname.user.domain.usecase.CreateUserUseCase;
import cz.radeknolc.appname.user.ui.dto.request.CreateUserRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Set;

import static cz.radeknolc.appname.shared.problem.domain.enumeration.ApiProblemCode.ACCOUNT_ALREADY_EXISTS;
import static cz.radeknolc.appname.shared.problem.domain.enumeration.ApiProblemCode.VALIDATION_ERROR;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class CreateUserControllerTest {

    @MockBean
    private CreateUserUseCase createUserUseCase;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_NotExistingUser_Success() throws Exception {
        // given
        CreateUserRequest createUserRequest = new CreateUserRequest("user", "user@example.com", "mysecretpassword");

        doNothing().when(createUserUseCase).createNewUser(any());

        // when
        ResultActions response = mockMvc.perform(post("/api/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequest)));

        // then
        response.andExpect(status().isOk());
    }

    @Test
    void register_AlreadyExistingUser_HandledProblem() throws Exception {
        // given
        ProblemCode problemCode = ACCOUNT_ALREADY_EXISTS;
        CreateUserRequest createUserRequest = new CreateUserRequest("user", "user@example.com", "mysecretpassword");

        doThrow(new Problem(problemCode)).when(createUserUseCase).createNewUser(any());

        // when
        ResultActions response = mockMvc.perform(post("/api/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequest)));

        // then
        response.andExpect(status().isBadRequest());
        response.andExpect(jsonPath("$.message").value(problemCode.toString()));
    }

    @Test
    void register_InvalidInputValue_HandledConstraintViolation() throws Exception {
        // given
        CreateUserRequest createUserRequest = new CreateUserRequest("", "", "");

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<CreateUserRequest>> constraintViolations = validator.validate(createUserRequest);

        doThrow(new ConstraintViolationException(constraintViolations)).when(createUserUseCase).createNewUser(any());

        // when
        ResultActions response = mockMvc.perform(post("/api/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequest)));

        // then
        response.andExpect(status().isUnprocessableEntity());
        response.andExpect(jsonPath("$.message").value(VALIDATION_ERROR.toString()));
        response.andExpect(jsonPath("$.errors.length()").value(3));
    }
}