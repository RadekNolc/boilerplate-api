package com.radeknolc.apiname.user.domain.usecase;

import com.radeknolc.apiname.user.ui.dto.request.CreateUserRequest;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface CreateUserUseCase {
    void createUser(@Valid CreateUserRequest createUserRequest);
}
