package com.radeknolc.apiname.user.domain.usecase;

import com.radeknolc.apiname.user.domain.entity.User;
import com.radeknolc.apiname.user.ui.dto.request.CreateUserRequest;

public interface UserUseCase {
    User createUser(CreateUserRequest createUserRequest);
}
