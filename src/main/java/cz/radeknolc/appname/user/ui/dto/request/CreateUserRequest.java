package cz.radeknolc.appname.user.ui.dto.request;

import cz.radeknolc.appname.shared.general.infrastructure.validation.NotExists;
import cz.radeknolc.appname.shared.general.infrastructure.validation.Password;
import cz.radeknolc.appname.user.infrastructure.persistence.entity.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotExists(entity = UserEntity.class, column = "username")
        @Size(min = 4, max = 16, message = "SIZE")
        String username,

        @NotBlank(message = "NOT_BLANK")
        @Email(message = "EMAIL")
        @Size(max = 64, message = "SIZE")
        String email,

        @Password(minSize = 12, minCapitals = 1, minNumbers = 1, minSpecials = 1)
        String password) {
}