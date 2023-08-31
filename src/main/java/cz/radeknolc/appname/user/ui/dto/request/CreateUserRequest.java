package cz.radeknolc.appname.user.ui.dto.request;

import cz.radeknolc.appname.shared.general.infrastructure.validation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @Size(min = 4, max = 16, message = "SIZE")
        String username,

        @NotBlank(message = "NOT_BLANK")
        @Email(message = "EMAIL")
        @Size(max = 64, message = "SIZE")
        String email,

        @Password(minCapitals = 1, minNumbers = 1, minSpecials = 1)
        @Size(min = 8, message = "SIZE")
        String password) {
}