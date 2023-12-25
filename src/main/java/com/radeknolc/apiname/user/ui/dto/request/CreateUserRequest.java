package com.radeknolc.apiname.user.ui.dto.request;

import com.radeknolc.apiname.authentication.infrastructure.validation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @Size(min = 4, max = 32, message = "SIZE")
        String username,

        @NotBlank(message = "NOT_BLANK")
        @Email(message = "EMAIL")
        String email,

        @Password(minSize = 12, minCapitals = 1, minNumbers = 1, minSpecials = 1)
        String password) {
}