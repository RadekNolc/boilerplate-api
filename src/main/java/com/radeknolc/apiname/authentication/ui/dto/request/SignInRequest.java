package com.radeknolc.apiname.authentication.ui.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
        @NotBlank(message = "NOT_BLANK")
        String username,

        @NotBlank(message = "NOT_BLANK")
        String password
) {
}