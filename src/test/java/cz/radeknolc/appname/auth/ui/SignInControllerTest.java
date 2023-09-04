package cz.radeknolc.appname.auth.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.radeknolc.appname.auth.domain.usecase.SignInUseCase;
import cz.radeknolc.appname.auth.ui.dto.request.SignInRequest;
import cz.radeknolc.appname.auth.ui.dto.response.SignInResponse;
import cz.radeknolc.appname.shared.problem.domain.enumeration.ApiProblemCode;
import cz.radeknolc.appname.shared.problem.domain.exception.Problem;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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

    @Test
    void signIn_WrongCredentials_HandledException() throws Exception {
        // given
        SignInRequest signInRequest = new SignInRequest("user", "wrongpassword");

        doThrow(new BadCredentialsException("Bad credentials")).when(signInUseCase).signIn(any());

        // when
        ResultActions response = mockMvc.perform(post("/api/authentication/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInRequest)));

        // then
        response.andExpect(status().isUnauthorized());
        response.andExpect(jsonPath("$.message").value(String.valueOf(ApiProblemCode.BAD_CREDENTIALS)));
    }

}