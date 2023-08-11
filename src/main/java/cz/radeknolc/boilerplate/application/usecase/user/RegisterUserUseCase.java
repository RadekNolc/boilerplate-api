package cz.radeknolc.boilerplate.application.usecase.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

@Validated
public interface RegisterUserUseCase {

    void registerNewUser(@Valid Request request);

    record Request(
            @NotBlank(message = "NOT_BLANK")
            @Size(min = 4, max = 16, message = "SIZE")
            String username,

            @NotBlank(message = "NOT_BLANK")
            @Email(message = "EMAIL")
            @Size(max = 64, message = "SIZE")
            String email,

            @NotBlank(message = "NOT_BLANK")
            @Size(min = 8, message = "SIZE")
            String password) {
    }
}
