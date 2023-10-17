package com.radeknolc.apiname.user.ui.dto.request;

import com.radeknolc.apiname.shared.general.infrastructure.validation.NotExists;
import com.radeknolc.apiname.shared.general.infrastructure.validation.Password;
import com.radeknolc.apiname.user.infrastructure.persistence.entity.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotExists(entity = UserEntity.class, column = "username")
        @Size(min = 4, max = 32, message = "SIZE")
        String username,

        @NotBlank(message = "NOT_BLANK")
        @Email(message = "EMAIL")
        String email,

        @Password(minSize = 12, minCapitals = 1, minNumbers = 1, minSpecials = 1)
        String password) {
}