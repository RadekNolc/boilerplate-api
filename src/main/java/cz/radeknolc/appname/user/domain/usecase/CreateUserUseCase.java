package cz.radeknolc.appname.user.domain.usecase;

import cz.radeknolc.appname.user.ui.dto.request.CreateUserRequest;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface CreateUserUseCase {
    void createNewUser(@Valid CreateUserRequest createUserRequest);
}
