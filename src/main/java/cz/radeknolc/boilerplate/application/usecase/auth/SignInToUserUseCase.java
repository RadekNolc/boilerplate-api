package cz.radeknolc.boilerplate.application.usecase.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public interface SignInToUserUseCase {

    Response signIn(@Valid Request request);

    record Request(
            @NotBlank(message = "NOT_BLANK")
            String username,

            @NotBlank(message = "NOT_BLANK")
            String password) { // TODO: Validation
    }

    record Response(String token) {}
}
